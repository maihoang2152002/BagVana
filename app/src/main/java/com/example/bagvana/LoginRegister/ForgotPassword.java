package com.example.bagvana.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bagvana.R;
import com.hbb20.CountryCodePicker;

public class ForgotPassword extends AppCompatActivity {
//    CountryCodePicker ccp;
    Button btnVerrifyFG;
    EditText phone ;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        phone = (EditText) findViewById(R.id.inputPhone) ;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);
        btnVerrifyFG = (Button) findViewById(R.id.forgotPassVerrify);
        btnVerrifyFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Số điện thoại không thể bỏ trống", Toast.LENGTH_LONG).show();
                }
                else{
                    String number = phone.getText().toString();
                    Intent intent = new Intent(ForgotPassword.this, OTPActivity.class);
                    intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ", ""));
                    intent.putExtra("type_numberphone","forgotpasswod");
                    startActivity(intent);
                }

            }
        });
    }
}