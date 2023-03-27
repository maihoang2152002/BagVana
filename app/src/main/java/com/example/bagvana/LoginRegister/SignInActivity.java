package com.example.bagvana.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bagvana.R;
import com.example.bagvana.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    EditText numberPhone;
    EditText password;
    Button loginButton;
    TextView signUp, forgotPass;

    FirebaseDatabase database ;
    DatabaseReference databasReference;
    private void initLogin(){
        loginButton = (Button) findViewById(R.id.btnSignIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             checkUser();
            }

        });
    };
    private void initSignUp(){
        signUp= (TextView) findViewById(R.id.textSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

        });
    };

    private void initForgotPass(){
        forgotPass= (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }

        });
    }

    private void checkUser(){
        numberPhone = (EditText) findViewById(R.id.inputNumberPhone);
        password = (EditText) findViewById(R.id.inputPassword);

        final CountryCodePicker ccp_su = (CountryCodePicker) findViewById(R.id.ccp);
        ccp_su.registerCarrierNumberEditText(numberPhone);
        String phone = ccp_su.getFullNumberWithPlus().replace(" ", "");
        String pass = password.getText().toString();
        Log.e("phone ", phone);
        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference("User");
        Query check = databasReference.orderByChild("phone").equalTo(phone);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String passwordValue = snapshot.child("password").getValue(String.class);
                    if(!Objects.equals(passwordValue,pass)){
                        Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                        startActivity(intent);
                    }
                    else{
                        password.setError("Mật khẩu không đúng");
                    }
                }
                else{
                    Toast.makeText(SignInActivity.this,"dang nhap that bai", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initComponents() {
        numberPhone = (EditText) findViewById(R.id.inputNumberPhone);
        password = (EditText) findViewById(R.id.txtConPassword);
//        User[] user = {new User("123", "bui thi dung", "0123432"),
//                new User("1234", "bui thi dung", "0123432"),
//                new User("1235", "bui thi dung", "0123432"),
//                new User("1237", "bui thi dung", "0123432")
//        };
        initLogin();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initForgotPass();
        initSignUp();
        initLogin();
//        initComponents();
    }
}