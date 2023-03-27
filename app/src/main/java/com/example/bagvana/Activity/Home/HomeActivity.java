package com.example.bagvana.Activity.Home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.HomeAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Product> productList;
    private HomeAdapter homeAdapter;
    private RecyclerView recycview_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recycview_home = findViewById(R.id.recycview_home);

        recycview_home.setHasFixedSize(true);
        recycview_home.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        homeAdapter = new HomeAdapter(this, productList);

        recycview_home.setAdapter(homeAdapter);

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}