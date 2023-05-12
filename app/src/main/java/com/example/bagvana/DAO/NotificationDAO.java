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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
                    try {
                        if(notification.getStatus().equals("0") && compareDate(notification.getTime())) {
                            size += 1;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    myCallback.onCallback(size);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
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
                    public void onCallback(long newNotification) throws ParseException {
                        int size = 0;
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            if(notification.getStatus().equals("0") && compareDate(notification.getTime())) {
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

    public boolean compareDate(String inputDate) throws ParseException {

        // Ép kiểu
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        Date date = inputDateFormat.parse(inputDate);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String outputDate = outputDateFormat.format(date);

        Date today = sdf.parse(outputDate);

        // So sánh
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -7);

        Date oneWeekAgo = calendar.getTime();

        if (today.compareTo(oneWeekAgo) > 0) {
            return false;
        }

        return true;
    }



    public interface MyCallback {
        void onCallback(long newNotification) throws ParseException;
    }
}
