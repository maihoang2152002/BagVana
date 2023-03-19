package com.example.bagvana;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    Button btnVerify;
    TextView txtErrorOtp;
    EditText otp;
    String phoneNumber,otpid;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        mAuth = FirebaseAuth.getInstance();

        phoneNumber = getIntent().getStringExtra("mobile").toString();
        otp = (EditText) findViewById(R.id.editOTP);
        btnVerify = (Button) findViewById(R.id.btnVerrify);
        txtErrorOtp = (TextView) findViewById(R.id.txtErrorOtp);
        initOTP();
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verrifyCode();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,otp.getText().toString());
                signInWithPhoneAuthCredential(credential);

//                txtErrorOtp.setText("Sai mã xác minh. Nhập lại! " + phoneNumber );
            }

        });


    }

    private void verrifyCode() {
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
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(OTPActivity.this,ChangePassword.class));
                            finish();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

//    private void senderVerrifyCode(String phoneNumber) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//            final String code = credential.getSmsCode();
//            if(code!= null){
//                verifyCode(code);
//            }
//
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(OTPActivity.this,"Verification Failed", Toast.LENGTH_LONG).show();
//
//        }
//        @Override
//        public void onCodeSent(@NonNull String s,
//                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
//            super.onCodeSent(s,  token);
//            otpId = s;
//        }
//    };

//    private void verifyCode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,code);
//    }


}