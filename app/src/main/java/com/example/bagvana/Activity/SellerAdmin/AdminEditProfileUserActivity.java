package com.example.bagvana.Activity.SellerAdmin;


import static com.example.bagvana.Utils.Utils._admin_list_user;
import static com.example.bagvana.Utils.Utils._list_user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;

import com.example.bagvana.Adapter.ListUserAdapter;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class AdminEditProfileUserActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ImageButton imgBtn_pickDate;
    EditText editTxt_DOB, editTxt_fullName, editTxtusername,
            editTxt_email, editTxt_phoneNumber;

    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnUpdateProfile;
    static User user = new User();
    private Toolbar toolbar;


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
                        AdminEditProfileUserActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        editTxt_fullName.setText(user.getFullname());
        editTxtusername.setText(user.getUsername());
        editTxt_email.setText(user.getEmail());
        editTxt_DOB.setText(user.getDob());
        editTxt_phoneNumber.setText(user.getPhone());
        if (Objects.equals(user.getGender(), "male")) {
            rgGender.check(R.id.rbMale);
        } else {
            rgGender.check(R.id.rbFemale);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String id = getIntent().getStringExtra("userId").toString();
        user = _admin_list_user.stream()
                .filter(user_t -> id.equals(user_t.getId()))
                .findAny()
                .orElse(null);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        editTxt_fullName = findViewById(R.id.editTxt_fullName);
        editTxtusername = findViewById(R.id.editTxt_username);
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
                noticeUpdate(isDataChanged());
            }
        });


    }
    private void noticeUpdate( boolean typeNotice) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if(typeNotice){
            alert.setMessage("Cập nhật người dùng thành công");
        }
        else{
            alert.setMessage("Không có gì thay đổi");
        }
        alert.show();
    }

    private boolean isDataChanged() {

        boolean isFullnameChanged = isFullnameChanged();
        boolean isUsernameChanged = isUsernameChanged();
        boolean isEmailChanged = isEmailChanged();
        boolean isDobChanged = isDobChanged();
        boolean isPhoneChanged = isPhoneChanged();
        boolean isGenderChanged = isGenderChanged();

        boolean hasChanges = isFullnameChanged || isUsernameChanged || isEmailChanged || isDobChanged
                || isPhoneChanged || isGenderChanged;

        if (hasChanges) {
            databaseReference.child(user.getId()).setValue(user);
            return true;
        } else {
            return false;
        }
    }

    private boolean isFullnameChanged() {
        if (!user.getFullname().equals(editTxt_fullName.getText().toString())) {
            user.setFullname(editTxt_fullName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isUsernameChanged() {
        if (!user.getUsername().equals(editTxtusername.getText().toString())) {
            user.setUsername(editTxtusername.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!user.getEmail().equals(editTxt_email.getText().toString())) {
            user.setEmail(editTxt_email.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isDobChanged() {
        if (!user.getDob().equals(editTxt_DOB.getText().toString())) {
            user.setDob(editTxt_DOB.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!user.getPhone().equals(editTxt_phoneNumber.getText().toString())) {
            user.setPhone(editTxt_phoneNumber.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        String temp;
        if (rbMale.isChecked()) temp = "male";
        else temp = "female";

        if (!user.getGender().equals(temp)) {
            user.setGender(temp);
            return true;
        } else {
            return false;
        }
    }

    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}