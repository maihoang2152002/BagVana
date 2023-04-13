package com.example.bagvana.Activity.ProductList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.bagvana.Utils.Utils._product_current;
public class UpdateProductActivity extends AppCompatActivity {
    public ImageButton image_product;

    private EditText name_product,color_product,price_product,description_product,quantity_product;
    private Button addBtn,cancelBtn;

    FirebaseDatabase database ;
    DatabaseReference databasReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);


        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference().child("Product");
        databasReference.child("p4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _product_current = snapshot.getValue(Product.class);
                Log.e("lau du lieu thanh on",""+_product_current.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }
//    public void viewInfoProduct(Product product){
////        image_product.setText(product.getName());
//        name_product.setText(product.getName());
//        color_product.setText(product.getColor());
//        price_product.setText(product.getPrice());
//        description_product.setText(product.getDescription());
//        quantity_product.setText(product.getAmount());
//
//    }
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