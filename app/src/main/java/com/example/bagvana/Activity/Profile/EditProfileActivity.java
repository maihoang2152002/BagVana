package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference databaseReference;

    ImageButton imgBtn_pickDate;
    EditText editTxt_DOB;
    EditText editTxt_fullName;
    EditText editTxt_username;
    EditText editTxt_email;
    EditText editTxt_phoneNumber;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnUpdateProfile;

    String fullnameUser, emailUser, usernameUser, dobUser, phoneUser, genderUser;

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

    private void showUserData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//
//                    String valueOfDataSnapshot = dataSnapshot.getValue().toString();
//                    switch (dataSnapshot.getKey()){
//                        case "fullname":
//                            editTxt_fullName.setText(valueOfDataSnapshot);
//                            fullnameUser = valueOfDataSnapshot;
//                            break;
//                        case "username":
//                            editTxt_username.setText(valueOfDataSnapshot);
//                            usernameUser = valueOfDataSnapshot;
//                            break;
//                        case "email":
//                            editTxt_email.setText(valueOfDataSnapshot);
//                            emailUser = valueOfDataSnapshot;
//                            break;
//                        case "dob":
//                            editTxt_DOB.setText(valueOfDataSnapshot);
//                            dobUser = valueOfDataSnapshot;
//                            break;
//                        case "phone":
//                            editTxt_phoneNumber.setText(valueOfDataSnapshot);
//                            phoneUser = valueOfDataSnapshot;
//                            break;
//                        case "gender":
//                            if (Objects.equals(valueOfDataSnapshot, "male")) {
//                                rgGender.check(R.id.rbMale);
//                            } else {
//                                rgGender.check(R.id.rbFemale);
//                            }
//                            genderUser = valueOfDataSnapshot;
//                            break;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        editTxt_fullName.setText(_user.getFullname());
        fullnameUser = _user.getFullname();
        editTxt_username.setText(_user.getUsername());
        usernameUser = _user.getUsername();
        editTxt_email.setText(_user.getEmail());
        emailUser = _user.getEmail();
        editTxt_DOB.setText(_user.getDob());
        dobUser = _user.getDob();
        editTxt_phoneNumber.setText(_user.getPhone());
        phoneUser = _user.getPhone();
        if (Objects.equals(_user.getGender(), "male")) {
            rgGender.check(R.id.rbMale);
        } else {
            rgGender.check(R.id.rbFemale);
        }
        genderUser = _user.getGender();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User").child(_user.getId());
        editTxt_fullName  = findViewById(R.id.editTxt_fullName);
        editTxt_username  = findViewById(R.id.editTxt_username);editTxt_username.setEnabled(false);
        editTxt_DOB = findViewById(R.id.editTxt_DOB);
        editTxt_email = findViewById(R.id.editTxt_email);
        editTxt_phoneNumber = findViewById(R.id.editTxt_phoneNumber);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        showUserData();
        initPickDate();

        editTxt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("email", "onClick: pressing emailEditText");
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullnameChanged() || isEmailChanged() || isDobChanged() || isPhoneChanged() || isGenderChanged()){
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isFullnameChanged() {
        if (!fullnameUser.equals(editTxt_fullName.getText().toString())){
            databaseReference.child("fullname").setValue(editTxt_fullName.getText().toString());
            fullnameUser = editTxt_fullName.getText().toString();
            return true;
        } else {
            return false;
        }
    }
    private boolean isEmailChanged() {
        if (!emailUser.equals(editTxt_email.getText().toString())){
            databaseReference.child("email").setValue(editTxt_email.getText().toString());
            emailUser = editTxt_email.getText().toString();
            return true;
        } else {
            return false;
        }
    }
    private boolean isDobChanged() {
        if (!dobUser.equals(editTxt_DOB.getText().toString())){
            databaseReference.child("dob").setValue(editTxt_DOB.getText().toString());
            dobUser = editTxt_DOB.getText().toString();
            return true;
        } else {
            return false;
        }
    }
    private boolean isPhoneChanged() {
        if (!phoneUser.equals(editTxt_phoneNumber.getText().toString())){
            databaseReference.child("phone").setValue(editTxt_phoneNumber.getText().toString());
            phoneUser = editTxt_phoneNumber.getText().toString();
            return true;
        } else {
            return false;
        }
    }
    private boolean isGenderChanged() {
        String temp ;
        if (rbMale.isChecked()) temp = "male";
        else temp = "female";

        if (!genderUser.equals(temp)){
            databaseReference.child("gender").setValue(temp);
            genderUser = temp;
            return true;
        } else {
            return false;
        }
    }
}