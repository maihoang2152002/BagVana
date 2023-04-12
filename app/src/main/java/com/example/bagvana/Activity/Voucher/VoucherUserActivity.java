package com.example.bagvana.Activity.Voucher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.bagvana.Adapter.DiscountShopAdapter;
import com.example.bagvana.Adapter.FreeshipShopAdapter;
import com.example.bagvana.DTO.User_Voucher;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VoucherUserActivity extends AppCompatActivity {

    private ArrayList<Voucher> freeshipVouchers;
    private ArrayList<Voucher> discountVouchers;
    private FreeshipShopAdapter freeshipShopAdapter;
    private DiscountShopAdapter discountShopAdapter;
    private RecyclerView recycview_freeshipVoucher;
    private RecyclerView recycview_discountVoucher;
    private TextView txt_voucherOfShop;
    private TextView txt_voucherOfUser;
    private Toolbar toolbar_voucher;
    private String type = "shop";
    private ArrayList<Voucher> vouchers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_user);

        recycview_freeshipVoucher = findViewById(R.id.recycview_freeshipVoucher);
        recycview_discountVoucher = findViewById(R.id.recycview_discountVoucher);

        txt_voucherOfShop = findViewById(R.id.txt_voucherOfShop);
        txt_voucherOfUser = findViewById(R.id.txt_voucherOfUser);

        // Freeship Voucher
        recycview_freeshipVoucher.setHasFixedSize(true);
        recycview_freeshipVoucher.setLayoutManager(new LinearLayoutManager(this));

        freeshipVouchers = new ArrayList<>();

        freeshipShopAdapter = new FreeshipShopAdapter((Context) this, freeshipVouchers, type);

        recycview_freeshipVoucher.setAdapter(freeshipShopAdapter);

        // Discount Voucher
        recycview_discountVoucher.setHasFixedSize(true);
        recycview_discountVoucher.setLayoutManager(new LinearLayoutManager(this));

        discountVouchers = new ArrayList<>();

        discountShopAdapter = new DiscountShopAdapter((Context) this, discountVouchers, type);

        recycview_discountVoucher.setAdapter(discountShopAdapter);

        initData();
        initDataOfUser();

        txt_voucherOfShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "shop";
                freeshipShopAdapter.setType(type);
                discountShopAdapter.setType(type);
                initData();
            }
        });

        txt_voucherOfUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "user";
                freeshipShopAdapter.setType(type);
                discountShopAdapter.setType(type);
                setDataVoucherOfUser();


            }
        });
    }

    private void initDataOfUser() {
        HashMap<String, Integer> user_vouchers = new HashMap<>();

        DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child("1");
        databaseReferenceUser_Voucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utils._vouchersOfUser.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User_Voucher user_voucher = dataSnapshot.getValue(User_Voucher.class);
                    Utils._vouchersOfUser.put(user_voucher.getVoucherID(), user_voucher.getAmount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDataVoucherOfUser() {
        HashMap<String, Integer> user_vouchers = new HashMap<>();

        DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child("1");
        databaseReferenceUser_Voucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utils._vouchersOfUser.clear();
                freeshipVouchers.clear();
                discountVouchers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User_Voucher user_voucher = dataSnapshot.getValue(User_Voucher.class);
                    user_vouchers.put(user_voucher.getVoucherID(), user_voucher.getAmount());
                    Utils._vouchersOfUser.put(user_voucher.getVoucherID(), user_voucher.getAmount());

                    for(Voucher voucher: vouchers) {
                        if(voucher.getId().equals(user_voucher.getVoucherID())) {
                            if(voucher.getType() == 2) {
                                int count = user_vouchers.get(voucher.getId());
                                while (count > 0) {
                                    freeshipVouchers.add(voucher);
                                    count -- ;
                                }

                            } else {
                                int count = user_vouchers.get(voucher.getId());
                                while (count > 0) {
                                    discountVouchers.add(voucher);
                                    count -- ;
                                }
                            }
                        }
                    }
                }
                freeshipShopAdapter.notifyDataSetChanged();
                discountShopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initData() {
        vouchers = new ArrayList<>();
        DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher");
        databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                freeshipVouchers.clear();
                discountVouchers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);

                    vouchers.add(voucher);

                    // check người dùng có lớn hơn amountOnPerson

                    if(voucher.getType() == 2) {
                        freeshipVouchers.add(voucher);
                    } else {
                        discountVouchers.add(voucher);
                    }
                }

                freeshipShopAdapter.notifyDataSetChanged();
                discountShopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}