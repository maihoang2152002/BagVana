package com.example.bagvana.Activity.ProductList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.bagvana.Utils.Utils.productList;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    public ImageButton image_product;

    private EditText name_product,color_product,price_product,description_product,quantity_product;
    private Button addBtn,cancelBtn;

    FirebaseDatabase database ;
    DatabaseReference databasReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        init();

        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference().child("Product");

        String name = name_product.getText().toString().trim();
        String color = color_product.getText().toString().trim();
        String description = description_product.getText().toString().trim();
        String price = price_product.getText().toString().trim();;

        String quanlity = quantity_product.getText().toString().trim();

        String id = Integer.toString(productList.size()+1);
        int amount = Integer.parseInt(quanlity);
        int gia = Integer.parseInt(price);
        Product product = new Product("p"+ id, name," ",color,description,amount,gia);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> hashMap = insertProduct(product);

                databasReference.child(product.getProductID()).setValue(hashMap).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.e("tao thanh cong",product.getProductID());
                    }
                });

            }
        });

    }
    public Map<String,Object> insertProduct(Product product){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("amount", product.getAmount());
        hashMap.put("color", product.getColor());
        hashMap.put("description", product.getDescription());
        hashMap.put("image", product.getImage());
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