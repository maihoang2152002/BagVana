package com.example.bagvana.Activity.Profile;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
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
import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.OrderList.OrderListActivity;
import com.example.bagvana.DTO.Order;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference databaseReferenceUser;
    ValueEventListener eventListener;

    LinearLayout linear_editProfile, linear_waitConfirmation,
            linear_waitDelivery, linear_delivered,
            linear_logOut, linear_changePassword;
    TextView txt_fullName, txt_amountProcessOrder,
            txt_amountDeliveryOrder, txt_amountDeliveredOrder;
    Toolbar toolbar;

    CircleImageView img_avatar;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txt_amountProcessOrder = findViewById(R.id.txt_amountProcessOrder);
        txt_amountDeliveryOrder = findViewById(R.id.txt_amountDeliveryOrder);
        txt_amountDeliveredOrder = findViewById(R.id.txt_amountDeliveredOrder);
        loadOrderAmount();

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        img_avatar = findViewById(R.id.img_avatar);
        // Retrieve Avatar from Firebase and Display
        if (!Objects.equals(_user.getAvatar(), "")){
            dialog.show();
            Glide.with(ProfileActivity.this).load(_user.getAvatar()).into(img_avatar);
            dialog.dismiss();
        }
        dialog.dismiss();

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
                        if (result.getResultCode() == Activity.RESULT_OK) {
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
                startActivityForResult(intent, 1);
            }
        });
        linear_logOut = findViewById(R.id.linear_logOut);
        linear_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _user.ResetUser();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        linear_changePassword = findViewById(R.id.linear_changePassword);
        linear_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
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
                intent.putExtra("title", "Đơn hàng chờ xác nhận");
                startActivity(intent);
            }
        });

        linear_waitDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("status", "2");
                intent.putExtra("title", "Đơn hàng chờ lấy hàng");
                startActivityForResult(intent, 2);
            }
        });

        linear_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("status", "3");
                intent.putExtra("title", "Đơn hàng đã giao");
                startActivity(intent);
            }
        });
    }

    private void loadOrderAmount() {
        DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int countProcess = 0, countDelivery = 0, countDelivered = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getUserID().equals(_user.getId())) {
                        if (order.getStatus().equals("1")) {
                            countProcess++;
                        } else if (order.getStatus().equals("2")) {
                            countDelivery++;
                        } else {
                            countDelivered++;
                        }
                    }
                }

                if (countProcess == 0) {
                    txt_amountProcessOrder.setVisibility(View.INVISIBLE);
                } else {
                    txt_amountProcessOrder.setVisibility(View.VISIBLE);
                    txt_amountProcessOrder.setText(String.valueOf(countProcess));
                }

                if (countDelivery == 0) {
                    txt_amountDeliveryOrder.setVisibility(View.INVISIBLE);
                } else {
                    txt_amountDeliveryOrder.setVisibility(View.VISIBLE);
                    txt_amountDeliveryOrder.setText(String.valueOf(countDelivery));
                }

                if (countDelivered == 0) {
                    txt_amountDeliveredOrder.setVisibility(View.INVISIBLE);
                } else {
                    txt_amountDeliveredOrder.setVisibility(View.VISIBLE);
                    txt_amountDeliveredOrder.setText(String.valueOf(countDelivered));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveData() {
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
                    while (!uriTask.isComplete()) ;
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

    public void uploadData() {
        FirebaseDatabase.getInstance().getReference("User").child(_user.getId()).child("avatar")
                .setValue(imageURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            _user.setAvatar(imageURL);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            loadOrderAmount();
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            txt_fullName.setText(_user.getFullname());
        }
    }

}

