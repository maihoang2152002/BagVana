package com.example.bagvana.Activity.Wishlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity implements ItemListener {
    private RecyclerView recyclerView;
    private ProductListAdapter mainAdapter;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                    Log.e("product", product.getName());
                }

                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mainAdapter = new ProductListAdapter(this, productList, this);
        recyclerView.setAdapter(mainAdapter);
    }


    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", productList.get(position));
        startActivity(intent);
    }
}