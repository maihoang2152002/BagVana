package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bagvana.R;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout linear_editProfile;
    TextView txt_fullName;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_fullName = findViewById(R.id.txt_fullName);
        txt_fullName.setText(_user.getFullname());

        linear_editProfile = findViewById(R.id.linear_editProfile);
        linear_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

}

