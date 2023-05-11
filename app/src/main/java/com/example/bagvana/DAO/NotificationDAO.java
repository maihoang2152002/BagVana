package com.example.bagvana.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bagvana.DTO.Notification;
import com.example.bagvana.DTO.User;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationDAO {

    DatabaseReference databaseReferenceNotification = FirebaseDatabase.getInstance().getReference("Notification");
    private ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationDAO() {};

    public void readNotificationOfNewProduct(MyCallback myCallback) {
        databaseReferenceNotification = FirebaseDatabase.getInstance().getReference("Notification").child("NewProduct").child(Utils._user.getId());
        databaseReferenceNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if(notification.getStatus().equals("0")) {
                        size += 1;
                    }
                }

                myCallback.onCallback(size);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readNotificationOfUpdateOrder(MyCallback myCallback) {
        databaseReferenceNotification = databaseReferenceNotification.child("UpdateOrder").child(Utils._user.getId());
        databaseReferenceNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                readNotificationOfNewProduct(new MyCallback() {
                    @Override
                    public void onCallback(long newNotification) {
                        int size = 0;
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            if(notification.getStatus().equals("0")) {
                                size += 1;
                            }
                        }
                        size += newNotification;
                        myCallback.onCallback(size);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public interface MyCallback {
        void onCallback(long newNotification);
    }
}
