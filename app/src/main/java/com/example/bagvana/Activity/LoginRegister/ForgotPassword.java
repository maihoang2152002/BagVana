package com.example.bagvana.Activity.LoginRegister;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;
import static com.example.bagvana.Utils.Utils._user_forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForgotPassword extends AppCompatActivity {
//    CountryCodePicker ccp;
    Button btnVerrifyFG;
    EditText phone ;
    CountryCodePicker ccp;
    DatabaseReference databaseReference;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                    String numberphone = ccp.getFullNumberWithPlus().replace(" ", "");


                    databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean existUser = false;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                User user = dataSnapshot.getValue(User.class);

                                if(numberphone.equals(user.getPhone())) {
                                    existUser = true;
                                    _user_forgot_password = user;
                                    Intent intent = new Intent(ForgotPassword.this, OTPActivity.class);
                                    intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ", ""));
                                    intent.putExtra("type_numberphone","forgotpassword");
                                    startActivity(intent);
                                    break;
                                }

                            }
                            if (existUser == false) {

                                noticeNotExitUser();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

            }
        });
    }
    private void noticeNotExitUser(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Số điện thoại không tồn tại. Vui lòng nhập lại");
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