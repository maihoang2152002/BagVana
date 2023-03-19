package com.example.bagvana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    EditText numberPhone;
    EditText password;
    Button loginButton;
    TextView signUp, forgotPass;
    private void initLogin(){
        loginButton = (Button) findViewById(R.id.btnSignIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username", numberPhone.getText().toString());
                bundle.putString("password", password.getText().toString());
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
    };

    private void initComponents() {
        numberPhone = (EditText) findViewById(R.id.inputNumberPhone);
        password = (EditText) findViewById(R.id.inputPassword);
        User[] user = {new User("123", "bui thi dung", "0123432"),
                new User("1234", "bui thi dung", "0123432"),
                new User("1235", "bui thi dung", "0123432"),
                new User("1237", "bui thi dung", "0123432")
        };
        initLogin();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initForgotPass();
        initSignUp();
//        initComponents();
    }
}