package com.example.bagvana.Activity.LoginRegister;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;
import static com.example.bagvana.Utils.Utils._user_forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Activity.Profile.ChangePasswordActivity;
import com.example.bagvana.Activity.Profile.ProfileActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword extends AppCompatActivity {
    EditText editTxt_oldPassword, editTxt_newPassword, editTxt_retypeNewPassword;
    Button btn_confirm;
    private Toolbar toolbar;
    String numberphone;

    DatabaseReference databasReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numberphone = getIntent().getStringExtra("numberphone").toString();
        editTxt_newPassword = findViewById(R.id.editTxt_newPassword);
        editTxt_retypeNewPassword = findViewById(R.id.editTxt_retypeNewPassword);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = editTxt_newPassword.getText().toString();
                String retypeNewPassword = editTxt_retypeNewPassword.getText().toString();
                if (newPassword.isEmpty()) {
                    editTxt_newPassword.setError("Mật khẩu không được bỏ trống");
                } else if (!newPassword.equals(retypeNewPassword)) {
                    editTxt_retypeNewPassword.setError("Mật khẩu không khớp");
                } else {

                    String resultPassword = convertHashToString(newPassword);
                    _user_forgot_password.setPassword(resultPassword);
                    databasReference = FirebaseDatabase.getInstance().getReference().child("User").child(_user_forgot_password.getId());

                    databasReference.setValue(_user_forgot_password) ;

                    Toast.makeText(ChangePassword.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePassword.this, SignInActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

        });


    }

    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
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
}