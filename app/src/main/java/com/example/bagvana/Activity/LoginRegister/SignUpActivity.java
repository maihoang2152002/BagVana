package com.example.bagvana.Activity.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private EditText numberPhone ;
    private EditText username ;
    private EditText password ;
    private EditText conPassword ;
    private Button btnSignUp;

    FirebaseDatabase database ;
    DatabaseReference databasReference;
    private void noticePhonenumberExit(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("Số điện thoại đã được đăng ký. Vui lòng nhập lại số điện thoại");
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView text ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        numberPhone = (EditText) findViewById(R.id.txtNumberPhone);
         username = (EditText) findViewById(R.id.txtUsername);
         password = (EditText) findViewById(R.id.txtPassword);
         conPassword = (EditText) findViewById(R.id.txtConPassword);
         btnSignUp  = (Button) findViewById(R.id.btnSignUp);


        final CountryCodePicker ccp_su = (CountryCodePicker) findViewById(R.id.ccp);
        ccp_su.registerCarrierNumberEditText(numberPhone);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data from EditText
                String usernameTxt = username.getText().toString();
                String passwordTxt = password.getText().toString();
                String conPasswordTxt = conPassword.getText().toString();
                String numberPhoneTxt = ccp_su.getFullNumberWithPlus().replace(" ", "");
                // check valid
                if( passwordTxt.isEmpty() ){
                    password.setError("Mật khẩu không khớp");
                }
                else if(!passwordTxt.equals(conPasswordTxt)){
                    conPassword.setError("Mật khẩu không khớp");

                }
                else if(usernameTxt.isEmpty()){
                    username.setError("Không được bỏ trống");
                }
                else if(numberPhoneTxt.isEmpty()){
                    numberPhone.setError("Không được bỏ trống");
                }
                else {
                    database = FirebaseDatabase.getInstance();
                    databasReference = database.getReference().child("User");
                    databasReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            int count = 1;
                            boolean exitUser = false;
                            if (task.isSuccessful()) {
                                List<String> posts = new ArrayList<>();

                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String phoneFB = ds.child("phone").getValue(String.class);
                                    if(numberPhoneTxt.equals(phoneFB)) {
                                        noticePhonenumberExit();
                                        exitUser = true;
                                        break;
                                    }
                                    count++;
                                }
                                if(!exitUser){
                                    Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                                    intent.putExtra("mobile",ccp_su.getFullNumberWithPlus().replace(" ", ""));
                                    String phoneNumber = ccp_su.getFullNumberWithPlus().replace(" ", "");
                                    String id = Integer.toString(count);
                                    intent.putExtra("type_numberphone","signup");
                                    intent.putExtra("id",id);
                                    intent.putExtra("username", usernameTxt);
                                    intent.putExtra("password", passwordTxt);
                                    if (getIntent().hasExtra("GetProductFromDeepLink")) {
                                        Product temp = (Product) getIntent().getSerializableExtra("GetProductFromDeepLink");
                                        intent.putExtra("GetProductFromDeepLink", temp);
                                    }
                                    startActivity(intent);
                                }
                                else{
                                    noticePhonenumberExit();
                                }

                            } else {
                                Log.e("k ton tai", usernameTxt);
                            }
                        }
                    });


                }
            }
        });
        text = (TextView) findViewById(R.id.textSignUp);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this,"dang nhan text", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


    }
}