package com.example.bagvana.Activity.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bagvana.R;
import com.example.bagvana.Activity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OTPActivity extends AppCompatActivity {
    Button btnVerify;
    TextView txtErrorOtp;
    EditText otp;
    String phoneNumber, phone_forgotpass,type_numbephone,otpid;
    FirebaseAuth mAuth;
    FirebaseDatabase database ;
    DatabaseReference databasReference; // = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bagvana-7335c-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference("User");

//        phoneNumber = getIntent().getStringExtra("mobile").toString();

        phoneNumber = getIntent().getStringExtra("mobile").toString();
        type_numbephone = getIntent().getStringExtra("type_numberphone").toString();

        otp = (EditText) findViewById(R.id.editOTP);
        btnVerify = (Button) findViewById(R.id.btnVerrify);
        txtErrorOtp = (TextView) findViewById(R.id.txtErrorOtp);

//        phoneNumber = phone_signup;

        initOTP();
        txtErrorOtp.setText("");
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                 forgotpassword
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,otp.getText().toString());
                signInWithPhoneAuthCredential(credential);




            }

        });


    }

    private void initOTP() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s,forceResendingToken);
                        otpid = s;
                    }

                    @Override

                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    private String convertHashToString(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(type_numbephone.equals("signup")){
                                String resultUsername =  getIntent().getStringExtra("username").toString();
                                String resultPass =  getIntent().getStringExtra("password").toString();
                                String id = getIntent().getStringExtra("id").toString();

                                //ma hoa mat khau
                                try {
                                    String pass_convert = convertHashToString(resultPass);

                                    User user = new User(id, phoneNumber, resultUsername,pass_convert,"","","","","");
                                    databasReference.child(id).setValue(user);
                                    startActivity(new Intent(OTPActivity.this,SignInActivity.class));

                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                            else if(type_numbephone.equals("forgotpassword")){
                                startActivity(new Intent(OTPActivity.this,ChangePassword.class));

                            }

                            finish();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                            txtErrorOtp.setText("Sai mã xác minh. Nhập lại! " + phone_forgotpass );

                        }
                    }
                });
    }



}