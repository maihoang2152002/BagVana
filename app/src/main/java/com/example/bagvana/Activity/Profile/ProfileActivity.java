package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.bagvana.Activity.OrderList.OrderListActivity;
import com.example.bagvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference databaseReferenceUser;
    ValueEventListener eventListener;

    LinearLayout linear_editProfile, linear_waitConfirmation,
            linear_waitDelivery, linear_delivered;
    TextView txt_fullName;
    Toolbar toolbar;

    ImageView img_avatar;
    String imageURL, oldImageURL = "";
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        img_avatar = findViewById(R.id.img_avatar);
        // Retrieve Avatar from Firebase and Display
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("User").child(_user.getId()).child("avatar");
        dialog.show();
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String avatarUrl = dataSnapshot.getValue(String.class);
//                if(oldImageURL == ""){
//                    oldImageURL = avatarUrl;
//                }
                Glide.with(ProfileActivity.this).load(avatarUrl).into(img_avatar);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_fullName = findViewById(R.id.txt_fullName);
        txt_fullName.setText(_user.getFullname());

        // Upload Avatar in Firebase Storage
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            img_avatar.setImageURI(uri);
                            saveData();
                        } else {
                            Toast.makeText(ProfileActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);

            }
        });


        linear_editProfile = findViewById(R.id.linear_editProfile);
        linear_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        linear_waitConfirmation = findViewById(R.id.linear_waitConfirmation);
        linear_waitDelivery = findViewById(R.id.linear_waitDelivery);
        linear_delivered = findViewById(R.id.linear_delivered);

        linear_waitConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("status", "1");
                startActivity(intent);
            }
        });

        linear_waitDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("status", "2");
                startActivity(intent);
            }
        });

        linear_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("status", "3");
                startActivity(intent);
            }
        });
    }

    public void saveData(){
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imagesAvatar")
                    .child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }

    }
    public void uploadData(){
        FirebaseDatabase.getInstance().getReference("User").child(_user.getId()).child("avatar")
                .setValue(imageURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            if (oldImageURL != "") {
//                                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
//                                reference.delete();
//                                oldImageURL = "";
//                            }
                            Toast.makeText(ProfileActivity.this, "Saved Avatar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

}

