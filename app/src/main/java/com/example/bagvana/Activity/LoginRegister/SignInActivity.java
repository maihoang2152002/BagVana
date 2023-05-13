package com.example.bagvana.Activity.LoginRegister;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.Activity.Chatbot.ChatActivity;
import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInActivity extends AppCompatActivity {

    EditText numberPhone;
    EditText password;
    Button loginButton;
    TextView signUp, forgotPass;
    ImageView visible;
    CheckBox checkSave;

    FirebaseDatabase database;
    DatabaseReference databasReference;
    String saveUser = "phone_password";
    boolean checkedVisibles = true;

    private void noticeNotExitUser(int type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        if (type == 1) {
            alert.setMessage("Số điện thoại hoặc mật khẩu không đúng. Vui lòng nhập lại");
        } else {
            alert.setMessage("Tài khoản đã bị khóa. Xin vui lòng đăng ký tài khoản mới");
        }

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

    private void initLogin() {
        loginButton = (Button) findViewById(R.id.btnSignIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();

            }

        });
    }

    ;

    private void initSignUp() {
        signUp = (TextView) findViewById(R.id.textSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product temp;
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                if (getIntent().hasExtra("GetProductFromDeepLink")) {
                    temp = (Product) getIntent().getSerializableExtra("GetProductFromDeepLink");
                    intent.putExtra("GetProductFromDeepLink", temp);
                    startActivity(intent);
                } else {

                    startActivity(intent);

                }
            }

        });
    }

    ;

    private void initForgotPass() {
        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }

        });
    }

    private void initVisiblePassword() {
        visible = (ImageView) findViewById(R.id.btn_visible);
        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedVisibles) {
                    visible.setImageResource(R.drawable.ic_visibility_off);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkedVisibles = false;
                } else {
                    visible.setImageResource(R.drawable.ic_see_password);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    checkedVisibles = true;

                }
            }
        });

    }

    private void checkUser() {

        final CountryCodePicker ccp_su = (CountryCodePicker) findViewById(R.id.ccp);
        ccp_su.registerCarrierNumberEditText(numberPhone);
        String phone = ccp_su.getFullNumberWithPlus().replace(" ", "");
        String pass = password.getText().toString();
        if (pass.isEmpty()) {
            password.setError("Mật khẩu không được bỏ trống");
        } else if (phone.isEmpty()) {
            numberPhone.setError("Số điện thoại không thể trống");

        } else {

            //            database = FirebaseDatabase.getInstance();
            databasReference = FirebaseDatabase.getInstance().getReference().child("User");

            databasReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean existUser = false;
                    int typeUser = 1;
                    _list_user.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);

//                        String passFB = dataSnapshot.child("password").getValue(String.class);
                        String pass_convert = convertHashToString(pass);
                        _list_user.add(user);

                        if (phone.equals(user.getPhone()) && pass_convert.equals(user.getPassword())) {
                            if (user.getStatus().equals("0")) {
                                typeUser = 0;
                            } else {
                                SharedPreferences sharedPreferences = getSharedPreferences(saveUser, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("numberPhone", phone);
                                editor.putString("password", pass);
                                editor.putBoolean("Save", checkSave.isChecked());
                                editor.commit();
                                existUser = true;
                                _user = user;
                                Log.e("user", _user.toString());
                                if (getIntent().hasExtra("GetProductFromDeepLink")) {
                                    Product temp = (Product) getIntent().getSerializableExtra("GetProductFromDeepLink");
                                    Intent intent = new Intent(SignInActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("product", temp);
                                    startActivityForResult(intent, 1);
                                } else if (_user.getTypeUser().equals("2")) {
                                    Intent intent = new Intent(SignInActivity.this, AdminHomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                }
                            }

                        }

                    }
                    if (existUser == false) {

                        noticeNotExitUser(typeUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void initComponents() {

        initForgotPass();
        initSignUp();
        initLogin();
        initVisiblePassword();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        numberPhone = (EditText) findViewById(R.id.inputNumberPhone);
        password = (EditText) findViewById(R.id.inputPassword);
        checkSave = (CheckBox) findViewById(R.id.checkBox);
        initComponents();
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(saveUser, MODE_PRIVATE);
        String phone = sharedPreferences.getString("numberPhone", "");
        String pass = sharedPreferences.getString("password", "");

        boolean save = sharedPreferences.getBoolean("Save", false);
        if (save == true) {
            checkSave.setChecked(true);
            numberPhone.setText(phone);
            password.setText(pass);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}