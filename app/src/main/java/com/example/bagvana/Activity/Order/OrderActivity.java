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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bagvana.Adapter.CartAdapter;
import com.example.bagvana.Adapter.OrderAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private ArrayList<Product> productList;
    private OrderAdapter orderAdapter;
    private RecyclerView recycview_order;
    private ArrayList<ReceiverInfo> receiverInfos;
    private TextView txt_productCost;
    private TextView txt_orderAddress;
    private LinearLayout linear_orderAddress;
    private LinearLayout linear_voucher;
    private LinearLayout linear_pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recycview_order = findViewById(R.id.recycview_order);
        txt_productCost = findViewById(R.id.txt_productCost);
        txt_orderAddress = findViewById(R.id.txt_orderAddress);

        linear_orderAddress = findViewById(R.id.linear_orderAddress);
        linear_voucher = findViewById(R.id.linear_voucher);
        linear_pay = findViewById(R.id.linear_pay);

        initOrderAddress();
        calBillCost();

        recycview_order.setHasFixedSize(true);
        recycview_order.setLayoutManager(new LinearLayoutManager(this));

        productList = (ArrayList<Product>) Utils.productList;

        orderAdapter = new OrderAdapter((Context) this, productList);

        recycview_order.setAdapter(orderAdapter);

        linear_orderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initOrderAddress() {
        receiverInfos = new ArrayList<>();

        // userID = 1
        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("ReceiverInfo").child("1");
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverInfos.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    ReceiverInfo receiverInfo = dataSnapshot.getValue(ReceiverInfo.class);
                    if(receiverInfo.isDefaultAddress()) {
                        String txt_address = receiverInfo.getFullName() + " | " + receiverInfo.getPhoneNumber() + '\n' + receiverInfo.getAddress();
                        txt_orderAddress.setText(txt_address);
                        break;
                    }

                    receiverInfos.add(receiverInfo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error:", error.getDetails());
            }
        });


    }

    private void calBillCost() {
        int billCost = 0;
        for(int i = 0; i < Utils.productList.size(); i++) {
            billCost += Utils.productList.get(i).getAmount() * Utils.productList.get(i).getPrice();
        }
        txt_productCost.setText(String.valueOf(billCost));
    }
}