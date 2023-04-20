package com.example.bagvana.Activity.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.bagvana.Adapter.DiscountVoucherAdapter;
import com.example.bagvana.Adapter.FreeshipVoucherAdapter;
import com.example.bagvana.DTO.User_Voucher;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChooseVoucherActivity extends AppCompatActivity {

    private ArrayList<Voucher> freeShipVouchers;
    private ArrayList<Voucher> discountVouchers;
    private FreeshipVoucherAdapter freeshipVoucherAdapter;
    private DiscountVoucherAdapter discountVoucherAdapter;
    private RecyclerView recyclerview_freeShipVoucher;
    private RecyclerView recyclerview_discountVoucher;

    private Button btn_complete;
    private Button btn_apply;
    private Toolbar toolbar_chooseVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_voucher);

        recyclerview_freeShipVoucher = findViewById(R.id.recycview_freeshipVoucher);
        recyclerview_discountVoucher = findViewById(R.id.recycview_discountVoucher);

        toolbar_chooseVoucher = findViewById(R.id.toolbar_chooseVoucher);
        setSupportActionBar(toolbar_chooseVoucher);

        btn_complete = findViewById(R.id.btn_complete);
        btn_apply = findViewById(R.id.btn_apply);

        // Freeship Voucher
        recyclerview_freeShipVoucher.setHasFixedSize(true);
        recyclerview_freeShipVoucher.setLayoutManager(new LinearLayoutManager(this));

        freeShipVouchers = new ArrayList<>();

        freeshipVoucherAdapter = new FreeshipVoucherAdapter((Context) this, freeShipVouchers);

        recyclerview_freeShipVoucher.setAdapter(freeshipVoucherAdapter);

        // Discount Voucher
        recyclerview_discountVoucher.setHasFixedSize(true);
        recyclerview_discountVoucher.setLayoutManager(new LinearLayoutManager(this));

        discountVouchers = new ArrayList<>();

        discountVoucherAdapter = new DiscountVoucherAdapter((Context) this, discountVouchers);

        recyclerview_discountVoucher.setAdapter(discountVoucherAdapter);

        initData();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseVoucherActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar_chooseVoucher) {
        toolbar_chooseVoucher.setNavigationIcon(R.drawable.ic_back);
        toolbar_chooseVoucher.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        HashMap<String, Integer> user_vouchers = new HashMap<>();

        DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child(Utils._user.getId());
        databaseReferenceUser_Voucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User_Voucher user_voucher = dataSnapshot.getValue(User_Voucher.class);
                    user_vouchers.put(user_voucher.getVoucherID(), user_voucher.getAmount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher");
        databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);

                    if(user_vouchers.containsKey(voucher.getId())) {

                        // kiểm tra hạn voucher
                        // start before day now and end after daynow

                        try {
                            if(compareDate(voucher.getStart(), voucher.getEnd()) == 0) {
                                if(voucher.getType() == 2) {
                                    int count = user_vouchers.get(voucher.getId());
                                    while (count > 0) {
                                        freeShipVouchers.add(voucher);
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
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                freeshipVoucherAdapter.notifyDataSetChanged();
                discountVoucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int compareDate(String start, String end) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date strDateEnd = sdf.parse(end);
        Date strDateStart = sdf.parse(start);

        // chưa xảy ra
        if (new Date().before(strDateStart) && new Date().before(strDateEnd)) {
            return 1;
        }

        // đã kết thúc
        if (new Date().after(strDateStart) && new Date().after(strDateEnd)) {
            return -1;
        }

        return 0;
    }

    public boolean checkDate(Voucher voucher) {
        String endDate = voucher.getEnd();
        return true;
    }
}