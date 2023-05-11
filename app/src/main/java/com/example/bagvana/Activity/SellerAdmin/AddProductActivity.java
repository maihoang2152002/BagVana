package com.example.bagvana.Activity.SellerAdmin;

import static com.example.bagvana.Utils.Utils._new_product;
import static com.example.bagvana.Utils.Utils._productList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.DAO.UserDAO;
import com.example.bagvana.DTO.Notification;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.example.bagvana.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    public ImageButton imageProduct;

    private EditText nameProduct,colorProduct,priceProduct,descriptionProduct,quantityProduct;
    private Button addBtn,cancelBtn;
    StorageReference storageReference;
    Uri imageUri;
    String image ,fileName;
    ProgressDialog progressDialog;
    FirebaseDatabase database ;
    Toolbar toolbar;
    DatabaseReference databasReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding.selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference().child("Product");
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();



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
                            progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();

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
                                addBtn =binding.addBtn;
                                cancelBtn = binding.cancelBtn;


                                String id = Integer.toString(_productList.size()+1);
                                if(check){
                                    Product product =  new Product("p"+id,nameProduct.getText().toString().trim(),image,
                                            colorProduct.getText().toString().trim(),descriptionProduct.getText().toString().trim(),
                                            Integer.parseInt(quantityProduct.getText().toString().trim()),Integer.parseInt(priceProduct.getText().toString().trim()),"1");
                                    Map<String,Object> hashMap = insertProduct(product);
                                    databasReference.child("p"+id).setValue(hashMap);
                                    noticeSuccess();

                                    //Notification new product
                                    notificationNewProduct(product);
                                }



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

    private void notificationNewProduct(Product product) {

        ArrayList<String> titles = new ArrayList<>();
        titles.add("Sản phẩm mới");
        titles.add("Mới! mới! mới!");
        titles.add("Xem sản phẩm nào bạn ơi!");
        titles.add("BagVana ra mắt sản phẩm mới");
        titles.add("Mua sắm ngay!");
        titles.add("Sản phẩm độc đáo");

        Random  randomGenerator = new Random();
        int index = randomGenerator.nextInt(titles.size());
        String title = titles.get(index);

        String message = product.getName() + " mới ra mắt tại BagVana. Nhấn xem để chi tiết thông sản phẩm";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        String time = mHour + ":" + mMinute + ":"  + " " + mDay + "/" + (mMonth + 1) + "/" + mYear;

        Notification notification = new Notification(product.getProductID(),title, message, product.getImage(), time, "0");

        DatabaseReference databasReferenceNotification = FirebaseDatabase.getInstance().getReference().child("Notification").child("NewProduct");

        UserDAO userDAO = new UserDAO();

        userDAO.readUser(new UserDAO.MyCallback() {

            @Override
            public void onCallback(ArrayList<User> users) {

                Map<String, Object> notificationValues = notification.toMap();

                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put(product.getProductID(), notificationValues);

                for(int i = 0; i < users.size(); i++) {
                    if(users.get(i).getTypeUser().equals("1")) {
                        String id = users.get(i).getId();
                        databasReferenceNotification.child(id).updateChildren(childUpdates);
                    }
                }
            }
        });
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();
            binding.imageProduct.setImageURI(imageUri);

        }
    }

    public Map<String,Object> insertProduct(Product product){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("amount", product.getAmount());
        hashMap.put("color", product.getColor());
        hashMap.put("price", product.getPrice());
        hashMap.put("description", product.getDescription());
        hashMap.put("image",product.getImage());
        hashMap.put("name", product.getName());
        hashMap.put("productID", product.getProductID());
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
        alert.setMessage("Them sản phẩm thành công ");
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