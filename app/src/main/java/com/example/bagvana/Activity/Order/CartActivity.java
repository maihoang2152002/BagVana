package com.example.bagvana.Activity.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bagvana.Adapter.CartAdapter;
import com.example.bagvana.DAO.CartDAO;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartDAO cartDAO;
    private ArrayList<Product> productList;
    private CartAdapter cartAdapter;
    private RecyclerView recycview_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycview_cart = findViewById(R.id.recycview_cart);

        recycview_cart.setHasFixedSize(true);
        recycview_cart.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        cartAdapter = new CartAdapter(this, productList);

        recycview_cart.setAdapter(cartAdapter);

        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart").child("1");
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}