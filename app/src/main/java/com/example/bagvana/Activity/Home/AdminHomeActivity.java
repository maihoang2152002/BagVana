package com.example.bagvana.Activity.Home;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.SellerAdmin.AdminConfirmActivity;
import com.example.bagvana.Activity.SellerAdmin.AdminProductListActivity;
import com.example.bagvana.Activity.SellerAdmin.ListUserActivity;
import com.example.bagvana.Activity.SellerAdmin.StatisticsActivity;
import com.example.bagvana.Activity.SellerAdmin.VoucherShopActivity;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    CardView card_statistics,list_product, card_confirm, card_settings, card_logout, card_list_user;
    DatabaseReference databasReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        card_statistics = findViewById(R.id.card_statistics);
        list_product = findViewById(R.id.list_product);
        card_confirm = findViewById(R.id.card_confirm);
        card_settings = findViewById(R.id.card_settings);
        card_logout = findViewById(R.id.card_logout);
        card_list_user = findViewById(R.id.list_user);

        databasReference = FirebaseDatabase.getInstance().getReference().child("User");

        Utils._admin_list_user = new ArrayList<>();
        databasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utils._admin_list_user.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    Utils._admin_list_user.add(user);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        card_statistics.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, StatisticsActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });

        list_product.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, AdminProductListActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });


        card_confirm.setOnClickListener(v -> {
            // code in here
            Intent intent = new Intent(AdminHomeActivity.this, AdminConfirmActivity.class);
            startActivity(intent);
        });

        card_settings.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, VoucherShopActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });
        card_list_user.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, ListUserActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });
        card_logout.setOnClickListener(v -> {
            // code in here
            _user.ResetUser();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finishAffinity();
        });

    }
}