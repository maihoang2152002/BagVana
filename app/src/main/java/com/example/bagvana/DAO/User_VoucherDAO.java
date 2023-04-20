package com.example.bagvana.DAO;

import androidx.annotation.NonNull;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User_Voucher;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_VoucherDAO {

    DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child(Utils._user.getId());
    private ArrayList<User_Voucher> user_vouches;

    public User_VoucherDAO() {

    }

    public void readData(MyCallback myCallback) {
        user_vouches = new ArrayList<>();
        databaseReferenceUser_Voucher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User_Voucher user_voucher = dataSnapshot.getValue(User_Voucher.class);

                    user_vouches.add(user_voucher);
                }

                myCallback.onCallback(user_vouches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setAmount(String voucherID) {

        readData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<User_Voucher> user_vouches) {
                for(User_Voucher uv: user_vouches) {
                    if(uv.getVoucherID().equals(voucherID)) {
                        int newAmount = uv.getAmount() - 1;
                        databaseReferenceUser_Voucher.child(uv.getVoucherID()).child("amount").setValue(newAmount);
                    }
                }

                return;
            }
        });
    }

    public interface MyCallback {
        void onCallback(ArrayList<User_Voucher> user_vouches);
    }
}
