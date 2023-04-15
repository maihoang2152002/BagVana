package com.example.bagvana.Activity.SellerAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bagvana.Adapter.CartAdapter;
import com.example.bagvana.Adapter.VoucherAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VoucherShopActivity extends AppCompatActivity {

    private ArrayList<Voucher> vouchers;
    private VoucherAdapter voucherAdapter;
    private RecyclerView recycview_voucher;
    private TextView txt_activeVoucher;
    private TextView txt_usedVoucher;
    private Button btn_create;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_shop);

        recycview_voucher = findViewById(R.id.recycview_voucher);

        txt_activeVoucher = findViewById(R.id.txt_activeVoucher);
        txt_usedVoucher = findViewById(R.id.txt_usedVoucher);

        btn_create = findViewById(R.id.btn_create);

        recycview_voucher.setHasFixedSize(true);
        recycview_voucher.setLayoutManager(new LinearLayoutManager(this));

        vouchers = new ArrayList<>();

        voucherAdapter = new VoucherAdapter(this, vouchers);

        recycview_voucher.setAdapter(voucherAdapter);

        initData();
        initChooseOption();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoucherShopActivity.this, CreateVoucherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initChooseOption() {
        txt_activeVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_activeVoucher.setTextColor(getResources().getColor(R.color.blue));
                txt_usedVoucher.setTextColor(getResources().getColor(R.color.black));
                type = 0;
                initData();
            }
        });

        txt_usedVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_activeVoucher.setTextColor(getResources().getColor(R.color.black));
                txt_usedVoucher.setTextColor(getResources().getColor(R.color.blue));
                type = 1;
                initData();
            }
        });

    }

    public boolean compareDate(String end) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date strDateEnd = sdf.parse(end);
        if (new Date().after(strDateEnd)) {
            return false;
        }

        return true;
    }

    private void initData() {
        DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher");
        databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vouchers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);

                    // voucher đg diễn ra
                    try {
                        if(type == 0 && compareDate(voucher.getEnd())) {
                            vouchers.add(voucher);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    //voucher đã kết thúc
                    try {
                        if(type == 1 && !compareDate(voucher.getEnd())) {
                            vouchers.add(voucher);

                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                voucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}