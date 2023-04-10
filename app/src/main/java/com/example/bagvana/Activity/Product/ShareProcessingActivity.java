package com.example.bagvana.Activity.Product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_processing);
        final Product[] curProduct = {new Product()};
//
//        Uri uri = getIntent().getData();
//        if (uri != null) {
//            String path = uri.toString();
//            String[] params = path.split("/");
//            Log.e("ProductID", params[params.length - 1]);
//
//            DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
//            Log.e("ProductID", "123");
//
//            databaseReferenceProduct.addValueEventListener(new ValueEventListener() {
//                @SuppressLint("NotifyDataSetChanged")
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.e("ProductID", "123");
//
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                        Product temp = dataSnapshot.getValue(Product.class);
//
//                        if (params[params.length - 1].equals(temp.getProductID())) {
//                            curProduct[0] = temp;
//                            Intent intent = new Intent(ShareProcessingActivity.this, ProductDetailActivity.class);
//                            intent.putExtra("product", curProduct[0]);
//                            startActivity(intent);
//                            break;
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            String path = appLinkData.toString();
            String[] params = path.split("/");
            Log.e("ProductID", params[params.length - 1]);

            DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
            Log.e("ProductID", "123");

            databaseReferenceProduct.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("ProductID", "123");

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Product temp = dataSnapshot.getValue(Product.class);

                        if (params[params.length - 1].equals(temp.getProductID())) {
                            curProduct[0] = temp;
                            Intent intent = new Intent(ShareProcessingActivity.this, ProductDetailActivity.class);
                            intent.putExtra("product", curProduct[0]);
                            startActivity(intent);
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}