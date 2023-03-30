package com.example.bagvana.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bagvana.R;

public class AddNewOrderAddressActivity extends AppCompatActivity {

    private String selectedCity, selectedDistrict, selectedWard;
    private TextView txt_fullName, txt_phone, txt_city, txt_district, txt_ward, txt_street;
    private Spinner spinner_city, spinner_district, spinner_ward;
    private ArrayAdapter<CharSequence> cityAdapter, districtAdapter, wardAdapter;
    private EditText editTxt_fullName;
    private EditText editTxt_phone;

    private EditText editTxt_street;
    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order_address);

        txt_city = findViewById(R.id.txt_city);
        txt_district = findViewById(R.id.txt_district);
        txt_ward = findViewById(R.id.txt_ward);
        txt_street = findViewById(R.id.txt_street);

        editTxt_fullName = findViewById(R.id.editTxt_fullName);
        editTxt_phone = findViewById(R.id.editTxt_phone);

        spinner_city = findViewById(R.id.spinner_city);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);
        editTxt_street = findViewById(R.id.editTxt_street);

        btn_complete = findViewById(R.id.btn_complete);

        cityAdapter = ArrayAdapter.createFromResource(this, R.array.array_vietnam_city, R.layout.spinner_layout);

        cityAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

        spinner_city.setAdapter(cityAdapter);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCity = spinner_city.getSelectedItem().toString();

                int cityID = adapterView.getId();

                if(cityID == R.id.spinner_city) {
                    switch (selectedCity) {
                        case "Lựa chọn Tỉnh hoặc Thành Phố":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_district, R.layout.spinner_layout);
                            break;
                        case "An Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_an_giang_district, R.layout.spinner_layout);
                            break;

                        default:
                            break;
                    }

                    districtAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

                    spinner_district.setAdapter(districtAdapter);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedDistrict = spinner_district.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCity.equals("Lựa chọn Tỉnh hoặc Thành Phố")) {
                    txt_city.setError("bắt buộc");
                    txt_city.requestFocus();
                }
            }
        });
    }



}