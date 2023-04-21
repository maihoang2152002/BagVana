package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._user;

import android.app.DatePickerDialog;
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

import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ImageButton imgBtn_pickDate;
    EditText editTxt_DOB, editTxt_fullName, editTxt_username,
            editTxt_email, editTxt_phoneNumber;

    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnUpdateProfile;
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
        editTxt_fullName.setText(_user.getFullname());
        editTxt_username.setText(_user.getUsername());
        editTxt_email.setText(_user.getEmail());
        editTxt_DOB.setText(_user.getDob());
        editTxt_phoneNumber.setText(_user.getPhone());
        if (Objects.equals(_user.getGender(), "male")) {
            rgGender.check(R.id.rbMale);
        } else {
            rgGender.check(R.id.rbFemale);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        editTxt_fullName = findViewById(R.id.editTxt_fullName);
        editTxt_username = findViewById(R.id.editTxt_username);
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
                if (isDataChanged()) {
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
            databaseReference.child(_user.getId()).setValue(_user);
            return true;
        } else {
            return false;
        }
    }

    private boolean isFullnameChanged() {
        if (!_user.getFullname().equals(editTxt_fullName.getText().toString())) {
            _user.setFullname(editTxt_fullName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isUsernameChanged() {
        if (!_user.getUsername().equals(editTxt_username.getText().toString())) {
            _user.setUsername(editTxt_username.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!_user.getEmail().equals(editTxt_email.getText().toString())) {
            _user.setEmail(editTxt_email.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isDobChanged() {
        if (!_user.getDob().equals(editTxt_DOB.getText().toString())) {
            _user.setDob(editTxt_DOB.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!_user.getPhone().equals(editTxt_phoneNumber.getText().toString())) {
            _user.setPhone(editTxt_phoneNumber.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        String temp;
        if (rbMale.isChecked()) temp = "male";
        else temp = "female";

        if (!_user.getGender().equals(temp)) {
            _user.setGender(temp);
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