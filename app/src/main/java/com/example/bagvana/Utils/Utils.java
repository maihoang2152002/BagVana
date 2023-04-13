package com.example.bagvana.Utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bagvana.Activity.Chatbot.ChatActivity;
import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Product> productList = new ArrayList<>();
    public static  User _user;
    public static Product _product_current = new Product();
    public static List<User> _list_user = new ArrayList<>();


    public static User findUserById(String id){
        FirebaseDatabase database ;
        DatabaseReference databasReference;
        User curr_user = null;

        database = FirebaseDatabase.getInstance();
        databasReference = database.getReference().child("User");

        databasReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean existUser = false;
                    for (DataSnapshot ds : task.getResult().getChildren()) {

                        String idFB = ds.child("id").getValue(String.class);
                        if(idFB.equals(id)) {
                            curr_user.setId(idFB);
                            curr_user.setPhone(ds.child("phone").getValue(String.class));
                            curr_user.setUsername(ds.child("username").getValue(String.class));
                            curr_user.setPassword(ds.child("password").getValue(String.class));
                            curr_user.setFullname( ds.child("fullname").getValue(String.class));
                            curr_user.setEmail( ds.child("email").getValue(String.class));
                            curr_user.setTypeUser(ds.child("typeUser").getValue(String.class));
                            curr_user.setAvatar(ds.child("avatar").getValue(String.class));
                            curr_user.setGender(ds.child("gender").getValue(String.class));
                            curr_user.setDob(ds.child("dob").getValue(String.class));


                        }
                    }

                }
            }
        });

        return curr_user;
    }
}
