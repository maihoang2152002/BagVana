package com.example.bagvana.DAO;


import androidx.annotation.NonNull;


import com.example.bagvana.DTO.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDAO {

    DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User");
    private ArrayList<User> users = new ArrayList<>();

    public UserDAO() {};

    public void readUser(MyCallback myCallback) {
        DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("User");
        databaseReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    users.add(user);
                }

                myCallback.onCallback(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(ArrayList<User> users);
    }
}
