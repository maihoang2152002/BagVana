package com.example.bagvana.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bagvana.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SortFilterActivity extends AppCompatActivity {
    private Chip chipTrending, chipNewArrival, chipBestSelling,
            chipLowToHigh, chipHighToLow,
            chipColorBlack, chipColorWhite, chipColorBlue;
    private Button btnApply;
    private ArrayList<String> selectedChipData;

    private Toolbar toolbar_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        toolbar_cart = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar_cart);

        chipTrending = findViewById(R.id.chipTrending);
        chipNewArrival = findViewById(R.id.chipNewArrival);
        chipBestSelling = findViewById(R.id.chipBestSelling);
        chipLowToHigh = findViewById(R.id.chipLowToHigh);
        chipHighToLow = findViewById(R.id.chipHighToLow);
        chipColorBlack = findViewById(R.id.chipColorBlack);
        chipColorWhite = findViewById(R.id.chipColorWhite);
        chipColorBlue = findViewById(R.id.chipColorBlue);
        btnApply = findViewById(R.id.btnApply);

        selectedChipData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        chipTrending.setOnCheckedChangeListener(checkedChangeListener);
        chipNewArrival.setOnCheckedChangeListener(checkedChangeListener);
        chipBestSelling.setOnCheckedChangeListener(checkedChangeListener);
        chipLowToHigh.setOnCheckedChangeListener(checkedChangeListener);
        chipHighToLow.setOnCheckedChangeListener(checkedChangeListener);
        chipColorBlack.setOnCheckedChangeListener(checkedChangeListener);
        chipColorWhite.setOnCheckedChangeListener(checkedChangeListener);
        chipColorBlue.setOnCheckedChangeListener(checkedChangeListener);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SortFilterActivity.this, selectedChipData.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_back);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}