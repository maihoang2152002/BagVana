package com.example.bagvana.Activity.Home;

import static com.example.bagvana.Utils.Utils._user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.SellerAdmin.AddProductActivity;
import com.example.bagvana.Activity.SellerAdmin.AdminConfirmActivity;
import com.example.bagvana.Activity.SellerAdmin.CreateVoucherActivity;
import com.example.bagvana.Activity.SellerAdmin.StatisticsActivity;
import com.example.bagvana.Activity.SellerAdmin.UpdateProductActivity;
import com.example.bagvana.Activity.SellerAdmin.VoucherShopActivity;
import com.example.bagvana.DTO.EventBus.VoucherCostEvent;
import com.example.bagvana.R;

public class AdminHomeActivity extends AppCompatActivity {

    CardView card_statistics,list_product, card_confirm, card_settings, card_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        card_statistics = findViewById(R.id.card_statistics);
        list_product = findViewById(R.id.list_product);
        card_confirm = findViewById(R.id.card_confirm);
        card_settings = findViewById(R.id.card_settings);
        card_logout = findViewById(R.id.card_logout);

        card_statistics.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, StatisticsActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });

        list_product.setOnClickListener(v -> {
            // code in here
            Intent myIntent = new Intent(AdminHomeActivity.this, AddProductActivity.class);
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
        card_logout.setOnClickListener(v -> {
            // code in here
            _user.ResetUser();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finishAffinity();
        });

    }
}