package com.example.bagvana.Activity.SellerAdmin;
import static com.example.bagvana.Utils.Utils._productList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.Activity.Home.HomeActivity;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;

import com.example.bagvana.Utils.Utils;
import com.example.bagvana.databinding.ActivityAddProductBinding;
import com.example.bagvana.databinding.ActivityUpdateProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {
    ActivityUpdateProductBinding binding;
    public ImageView imageProduct;

    private EditText nameProduct,colorProduct,priceProduct,descriptionProduct,quantityProduct;
    private Button addBtn,cancelBtn;
    StorageReference storageReference;
    Uri imageUri;
    String image ,fileName;
    ProgressDialog progressDialog;
    FirebaseDatabase database ;
    DatabaseReference databasReference;
    Product _product_current;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String id_product = getIntent().getStringExtra("id_product").toString();

        imageProduct = binding.imageProduct;
        nameProduct = binding.nameProduct;
        colorProduct = binding.colorProduct;
        priceProduct = binding.priceProduct;
        descriptionProduct = binding.descriptionProduct;
        quantityProduct = binding.quantityProduct;
        addBtn =binding.addBtn;
        cancelBtn = binding.cancelBtn;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databasReference = FirebaseDatabase.getInstance().getReference("Product").child(id_product);
        databasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               _product_current = snapshot.getValue(Product.class);
//                Log.e("tyoe", ""+ _product_current.getPrice());
//                _product_current =_product_current;


                Glide.with(UpdateProductActivity.this)
                        .load(_product_current.getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageProduct);

                binding.nameProduct.setText(_product_current.getName());
                binding.colorProduct.setText(_product_current.getColor());
                binding.priceProduct.setText(String.valueOf(_product_current.getPrice()));
                binding.descriptionProduct.setText(_product_current.getDescription());
                binding.quantityProduct.setText(String.valueOf(_product_current.getAmount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    image = _product_current.getImage();
                } else {
                    uploadImage();
                }
                boolean check = true;
                if (binding.nameProduct.getText().toString().trim().isEmpty()) {
                    binding.nameProduct.setError("Thông tin không được bỏ trống");
                    check = false;
                }
                if (binding.colorProduct.getText().toString().trim().isEmpty()) {
                    binding.colorProduct.setError("Thông tin không được bỏ trống");
                    check = false;
                }
                if (binding.descriptionProduct.getText().toString().trim().isEmpty()) {
                    binding.descriptionProduct.setError("Thông tin không được bỏ trống");
                    check = false;
                }
                if (binding.quantityProduct.getText().toString().trim().isEmpty()) {
                    binding.quantityProduct.setError("Thông tin không được bỏ trống");
                    check = false;
                }

                imageProduct = binding.imageProduct;
                nameProduct = binding.nameProduct;
                colorProduct = binding.colorProduct;
                priceProduct = binding.priceProduct;
                descriptionProduct = binding.descriptionProduct;
                quantityProduct = binding.quantityProduct;
                if(check){
                    Product product = new Product(_product_current.getProductID(),nameProduct.getText().toString().trim(),image,
                            colorProduct.getText().toString().trim(),descriptionProduct.getText().toString().trim(),
                            Integer.parseInt(quantityProduct.getText().toString().trim()),Integer.parseInt(priceProduct.getText().toString().trim()),"1");

                    Map<String, Object> updateValues = insertProduct(product);

                    databasReference = FirebaseDatabase.getInstance().getReference("Product").child(_product_current.getProductID());

                    databasReference.setValue(updateValues);
                    if (imageUri == null) {
                        finish();
                        Intent intent = new Intent(UpdateProductActivity.this, AdminProductListActivity.class);
                        startActivity(intent);
                    }
                }
            }

        });
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProductActivity.this, AdminProductListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setSupportActionBar(Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_back);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date now = new Date();
        String fileName = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("imagesProduct/"+fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        binding.imageProduct.setImageURI(null);
                        if (progressDialog.isShowing())
                            progressDialog.  dismiss();
                        Toast.makeText(UpdateProductActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                        File localFile;
                        try {
                            localFile = File.createTempFile(fileName, "");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageProduct.setImageBitmap(bitmap);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(UpdateProductActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


                    }
                });

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();
            binding.imageProduct.setImageURI(imageUri);
            uploadImage();
        }
    }
    private void uploadData() {

    }
    public Map<String,Object> insertProduct(Product product){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("amount",product.getAmount());
        hashMap.put("color", product.getColor());
        hashMap.put("price", product.getPrice());
        hashMap.put("description", product.getDescription());
        hashMap.put("image",product.getImage());
        hashMap.put("name", product.getName());
        hashMap.put("productID", product.getProductID());
        hashMap.put("rating", product.getRating());
        hashMap.put("status", product.getStatus());


        return hashMap;
    }
    private void noticeSuccess(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Chỉnh sửa sản phẩm thành công ");
        alert.show();
    }
    private void noticeDeleteSuccess(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Xóa sản phẩm thành công ");
        alert.show();
    }
    private void noticeFail(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Thất bại");
        alert.show();
    }


}