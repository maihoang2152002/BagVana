package com.example.bagvana.Activity.LoginRegister;

import static com.example.bagvana.Utils.Utils._user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInActivity extends AppCompatActivity {

    EditText numberPhone;
    EditText password;
    Button loginButton;
    TextView signUp, forgotPass;

    FirebaseDatabase database ;
    DatabaseReference databasReference;

    private void noticeNotExitUser(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Số điện thoại hoặc mật khẩu không đúng. Vui lòng nhập lại");
        alert.show();
    }
    private String convertHashToString(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }
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
        if( pass.isEmpty() ){
            password.setError("Mật khẩu không được bỏ trống");
        }
        else if(phone.isEmpty()){
            numberPhone.setError("Số điện thoại không thể trống");

        }
        else{
            database = FirebaseDatabase.getInstance();
            databasReference = database.getReference().child("User");
            databasReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean existUser = false;
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            User user = ds.getValue(User.class);
                            String pass_convert = convertHashToString(pass);
                            Log.e("pass", user.getPassword());
                            if(phone.equals(user.getPhone()) && pass_convert.equals(user.getPassword())) {
                                existUser = true;
                                _user = user;
                                Log.e("user",_user.toString());
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                break;
                            }
                        }
                        if (existUser == false) {
                            noticeNotExitUser();
                        }
                    }
                }
            });
        }
    }

    private void initComponents() {

        initForgotPass();
        initSignUp();
        initLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initComponents();
    }
}