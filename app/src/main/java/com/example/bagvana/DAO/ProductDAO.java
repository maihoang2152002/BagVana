package com.example.bagvana.DAO;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bagvana.Activity.Order.OrderActivity;
import com.example.bagvana.DTO.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDAO {

    DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
    private ArrayList<Product> products;

    public ProductDAO() {

    }

    public void readData(MyCallback myCallback) {
        products = new ArrayList<>();
        products.clear();
        databaseReferenceProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);

                    products.add(product);
                }

                myCallback.onCallback(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void setAmount(Product product) {

        DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");


        readData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                for(Product pd: products) {
                    if(pd.getProductID().equals(product.getProductID())) {
                        int newAmount = pd.getAmount() - product.getAmount();
                        databaseReferenceProduct.child(product.getProductID()).child("amount").setValue(newAmount);
                    }
                }

                return;
            }
        });
    }

    public interface MyCallback {
        void onCallback(ArrayList<Product> products);
    }

}
