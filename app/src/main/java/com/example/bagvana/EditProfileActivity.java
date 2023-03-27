package com.example.bagvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");



        EditText editTxt_fullName = findViewById(R.id.editTxt_fullName);
        EditText editTxt_NickName = findViewById(R.id.editTxt_NickName);
        EditText editTxt_DOB = findViewById(R.id.editTxt_DOB);
        EditText editTxt_email = findViewById(R.id.editTxt_email);
        EditText editTxt_phoneNumber = findViewById(R.id.editTxt_phoneNumber);
        EditText editTxt_gender = findViewById(R.id.editTxt_gender);

        String id = "dungbui";
        Query checkUserDatabase  = databaseReference.orderByChild("id").equalTo(id);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String fullnameFromDB = snapshot.child(id).child("fullname").getValue(String.class);
                    String emailFromDB = snapshot.child(id).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(id).child("username").getValue(String.class);
                    String dobFromDB = snapshot.child(id).child("dob").getValue(String.class);
                    String phoneFromDB = snapshot.child(id).child("phone").getValue(String.class);
                    String genderFromDB = snapshot.child(id).child("gender").getValue(String.class);
//                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
//                        startActivity(intent);


                    editTxt_fullName.setText(fullnameFromDB);
                    editTxt_NickName.setText(usernameFromDB);
                    editTxt_email.setText(emailFromDB);
                    editTxt_DOB.setText(dobFromDB);
                    editTxt_phoneNumber.setText(phoneFromDB);
                    editTxt_gender.setText(genderFromDB);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}