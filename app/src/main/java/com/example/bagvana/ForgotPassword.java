package com.example.bagvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

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
                String number = phone.getText().toString();
                Intent intent = new Intent(ForgotPassword.this, OTPActivity.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ", ""));
                startActivity(intent);
            }
        });
    }



}