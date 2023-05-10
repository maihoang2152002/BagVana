package com.example.bagvana.Activity.SellerAdmin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.R;
import com.example.bagvana.fragments.StatisticsFragment;

public class StatisticsActivity extends AppCompatActivity {

    ImageView back;
    TextView total, now, yesterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        loadFragment(new StatisticsFragment("0"));

        back = findViewById(R.id.imageView);
        total = findViewById(R.id.total);
        now = findViewById(R.id.now);
        yesterday = findViewById(R.id.yesterday);

        back.setOnClickListener(v -> {
            Intent myIntent = new Intent(StatisticsActivity.this, AdminHomeActivity.class);
            finish();
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });

        total.setOnClickListener(v -> {
            loadFragment(new StatisticsFragment("0"));
            total.setTextColor(Color.parseColor("#ffffff"));
            now.setTextColor(Color.parseColor("#B2AFAF"));
            yesterday.setTextColor(Color.parseColor("#B2AFAF"));
        });
        now.setOnClickListener(v -> {
            loadFragment(new StatisticsFragment("1"));
            now.setTextColor(Color.parseColor("#ffffff"));
            total.setTextColor(Color.parseColor("#B2AFAF"));
            yesterday.setTextColor(Color.parseColor("#B2AFAF"));
        });
        yesterday.setOnClickListener(v -> {
            loadFragment(new StatisticsFragment("-1"));
            yesterday.setTextColor(Color.parseColor("#ffffff"));
            now.setTextColor(Color.parseColor("#B2AFAF"));
            total.setTextColor(Color.parseColor("#B2AFAF"));
        });

    }

    void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}
