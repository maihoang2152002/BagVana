package com.example.bagvana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btn;
        TextView text ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn = (Button) findViewById(R.id.btnSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this,"ddang nhan nut", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                startActivity(intent);
            }
        });
        text = (TextView) findViewById(R.id.textSignUp);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this,"ddang nhan text", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


    }
}