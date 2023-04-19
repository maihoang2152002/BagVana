package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;

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
import com.example.bagvana.Activity.LoginRegister.OTPActivity;
import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.LoginRegister.SignUpActivity;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
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

public class ChangePasswordActivity extends AppCompatActivity {
    EditText editTxt_oldPassword, editTxt_newPassword, editTxt_retypeNewPassword;
    Button btn_confirm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTxt_oldPassword = findViewById(R.id.editTxt_oldPassword);
        editTxt_newPassword = findViewById(R.id.editTxt_newPassword);
        editTxt_retypeNewPassword = findViewById(R.id.editTxt_retypeNewPassword);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = editTxt_oldPassword.getText().toString();
                if (oldPass.isEmpty()) {
                    editTxt_oldPassword.setError("Mật khẩu không được bỏ trống");
                } else {

                    String old_pass_convert = convertHashToString(oldPass);
                    if (old_pass_convert.equals(_user.getPassword())) {
//                        Log.e("oldPass", "Mat khau cu dung roi");
                        String newPassword = editTxt_newPassword.getText().toString();
                        String retypeNewPassword = editTxt_retypeNewPassword.getText().toString();
                        if (newPassword.isEmpty()) {
                            editTxt_newPassword.setError("Mật khẩu không được bỏ trống");
                        } else if (!newPassword.equals(retypeNewPassword)) {
                            editTxt_retypeNewPassword.setError("Mật khẩu không khớp");
                        } else {
                            String resultPassword = convertHashToString(newPassword);
                            FirebaseDatabase.getInstance().getReference("User").child(_user.getId()).child("password")
                                    .setValue(resultPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChangePasswordActivity.this, "Change password successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChangePasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }


                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ChangePasswordActivity.this);
                        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.setMessage("Mật khẩu cũ không đúng. Vui lòng nhập lại");
                        alert.show();
                    }

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