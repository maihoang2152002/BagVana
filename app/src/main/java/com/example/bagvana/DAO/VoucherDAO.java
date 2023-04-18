package com.example.bagvana.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bagvana.Activity.Voucher.VoucherUserActivity;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Voucher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class VoucherDAO {
    DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher");
    private ArrayList<Voucher> vouchers;

    public VoucherDAO() {
        vouchers = new ArrayList<>();
        databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    vouchers.add(voucher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean isExisted(Voucher voucher) {
        for(Voucher vou: vouchers) {
            if(voucher.getId() == vou.getId()) {
                return true;
            }
        }
        return false;
    }

    public void addValue(Voucher voucher) {
        databaseReferenceVoucher.child(voucher.getId()).setValue(voucher);
    }

    public void readCountSavedVoucher(Voucher voucher, MyCallback myCallback) {
        DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher");
        databaseReferenceUser_Voucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    databaseReferenceUser_Voucher.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readCountUsedVoucher(Voucher voucher, MyCallback myCallback) {
        DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);

                    if (order.getUsedVoucher() != null) {

                        Set<String> voucherIDSet = order.getUsedVoucher().keySet();
                        for (String voucherID : voucherIDSet) {
                            if (voucherID.equals(voucher.getId())) {
                                count += 1;
                            }
                        }

                    }
                }

                myCallback.onCallback(voucher.getId(), count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(String voucherID, int value);
    }
}
