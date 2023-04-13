package com.example.bagvana.Activity.ProductList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static com.example.bagvana.Utils.Utils._product_current;
import static com.example.bagvana.Utils.Utils.productList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    public ImageButton image_product;

    private EditText name_product,color_product,price_product,description_product,quantity_product;
    private Button addBtn,cancelBtn;
    StorageReference storageReference;
    Uri imageUri;
    String image ,fileName;
    ProgressDialog progressDialog;
    FirebaseDatabase database ;
    DatabaseReference databasReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        init();




        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference().child("Product");
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
                Log.e("link2 ",""+image);


            }
        });

    }
    private void uploadImage() {

        String name = name_product.getText().toString().trim();
        String color = color_product.getText().toString().trim();
        String description = description_product.getText().toString().trim();
        String price = price_product.getText().toString().trim();;

        String quanlity = quantity_product.getText().toString().trim();

        String id = Integer.toString(productList.size()+1);
        int amount = Integer.parseInt(quanlity);
        int gia = Integer.parseInt(price);

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
                            progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                        File localFile;
                        try {
                            localFile = File.createTempFile(fileName, "");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    image_product.setImageBitmap(bitmap);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();
                                Log.e("link",image);
                                _product_current = new Product("p9",name,image,color,description,amount,gia);
                                Map<String,Object> hashMap = insertProduct(_product_current);
                                databasReference.child(_product_current.getProductID()).setValue(hashMap);
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
                        Toast.makeText(AddProductActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


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

        }
    }

    public Map<String,Object> insertProduct(Product product){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("amount", Integer.toString(product.getAmount()));
        hashMap.put("color", product.getColor());
        hashMap.put("price", Integer.toString(product.getPrice()));
        hashMap.put("description", product.getDescription());
        hashMap.put("image",product.getImage());
        hashMap.put("name", product.getName());
        hashMap.put("productID", product.getProductID());

        return hashMap;
    }
    public void init(){
        image_product = (ImageButton) findViewById(R.id.image_product);
        name_product = (EditText) findViewById(R.id.name_product);
        color_product = (EditText) findViewById(R.id.color_product);
        price_product = (EditText) findViewById(R.id.price_product);
        description_product = (EditText) findViewById(R.id.description_product);
        quantity_product = (EditText) findViewById(R.id.quantity_product);
        addBtn = (Button) findViewById(R.id.addBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

    }
}