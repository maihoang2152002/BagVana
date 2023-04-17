package com.example.bagvana.Activity.Product;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class ShareProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_processing);
        final Product[] curProduct = {new Product()};

        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.toString();
            String[] params = path.split("/");
            Log.e("ProductID", params[params.length - 1]);

            DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
            Log.e("ProductID", "123");

            databaseReferenceProduct.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("ProductID", "123");

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Product temp = dataSnapshot.getValue(Product.class);

                        if (params[params.length - 1].equals(temp.getProductID())) {
                            curProduct[0] = temp;
                            Intent intent = new Intent(ShareProcessingActivity.this, SignInActivity.class);
                            intent.putExtra("GetProductFromDeepLink", curProduct[0]);
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


        // ATTENTION: This was auto-generated to handle app links.
//        Intent appLinkIntent = getIntent();
//        String appLinkAction = appLinkIntent.getAction();
//        Uri appLinkData = appLinkIntent.getData();
//        if (appLinkData != null) {
//            String path = appLinkData.toString();
//            String[] params = path.split("/");
//            Log.e("ProductID", params[params.length - 1]);
//
//            DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
//
//            databaseReferenceProduct.addValueEventListener(new ValueEventListener() {
//                @SuppressLint("NotifyDataSetChanged")
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
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
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }

//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        Log.e("ShareprocessActivity", "We have a dynamic link");
//                        // Get deep link from result (may be null if no link is found)
//                        Uri deepLink = null;
//                        if (pendingDynamicLinkData != null) {
//                            deepLink = pendingDynamicLinkData.getLink();
//                        }
//
//                        Log.e("Deeplink", String.valueOf(deepLink));
//                        Intent intent = new Intent(ShareProcessingActivity.this, SignInActivity.class);
//                        startActivity(intent);
////                        if (deepLink != null) {
////                            String path = deepLink.toString();
////                            String[] params = path.split("/");
////                            Log.e("ProductID", params[params.length - 1]);
////
////                            DatabaseReference databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");
////
////                            databaseReferenceProduct.addValueEventListener(new ValueEventListener() {
////                                @SuppressLint("NotifyDataSetChanged")
////                                @Override
////                                public void onDataChange(@NonNull DataSnapshot snapshot) {
////
////                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
////
////                                        Product temp = dataSnapshot.getValue(Product.class);
////
////                                        if (params[params.length - 1].equals(temp.getProductID())) {
////                                            curProduct[0] = temp;
////                                            Intent intent = new Intent(ShareProcessingActivity.this, ProductDetailActivity.class);
////                                            intent.putExtra("product", curProduct[0]);
////                                            startActivity(intent);
////                                            break;
////                                        }
////
////                                    }
////
////                                }
////
////                                @Override
////                                public void onCancelled(@NonNull DatabaseError error) {
////
////                                }
////                            });
////                        }
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "getDynamicLink:onFailure", e);
//                    }
//                });
    }

    }