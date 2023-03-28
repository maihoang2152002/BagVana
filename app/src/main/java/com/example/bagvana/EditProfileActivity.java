package com.example.bagvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference databaseReference;

    ImageButton imgBtn_pickDate;
    EditText editTxt_DOB;
    EditText editTxt_fullName;
    EditText editTxt_NickName;
    EditText editTxt_email;
    EditText editTxt_phoneNumber;
    RadioGroup rgGender;

    private void initPickDate() {
        imgBtn_pickDate = (ImageButton) findViewById(R.id.imgBtn_pickDate);
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        imgBtn_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        editTxt_DOB.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initPickDate();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        editTxt_fullName  = findViewById(R.id.editTxt_fullName);
        editTxt_NickName  = findViewById(R.id.editTxt_NickName);
        editTxt_DOB = findViewById(R.id.editTxt_DOB);
        editTxt_email = findViewById(R.id.editTxt_email);
        editTxt_phoneNumber = findViewById(R.id.editTxt_phoneNumber);
        rgGender = findViewById(R.id.rgGender);



        String id = "duydo";
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
                    if (Objects.equals(genderFromDB, "male")){
                        rgGender.check(R.id.rbMale);
                    } else {
                        rgGender.check(R.id.rbFemale);
                    }


                }
                else {
                    Toast.makeText(EditProfileActivity.this,"Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}