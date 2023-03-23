package com.example.bagvana.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bagvana.DTO.Cart;
import com.example.bagvana.DTO.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private ArrayList<Product> productList = new ArrayList<>();
    DatabaseReference databaseReferenceCart;

    public CartDAO(String userID) {
        databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart").child(userID);
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                    Log.e("DAO", String.valueOf(productList.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<Product> getProductList() {
        Log.e("Product list", String.valueOf(productList.size()));
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
