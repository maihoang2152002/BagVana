package com.example.bagvana.DAO;

import androidx.annotation.NonNull;

import com.example.bagvana.Activity.Voucher.VoucherUserActivity;
import com.example.bagvana.DTO.Voucher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
}
