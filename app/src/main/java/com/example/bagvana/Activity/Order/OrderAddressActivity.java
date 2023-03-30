package com.example.bagvana.Activity.Order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.OrderAddressAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderAddressActivity extends AppCompatActivity {

    private ArrayList<ReceiverInfo> receiverInfos;
    private OrderAddressAdapter orderAddressAdapter;
    private RecyclerView recycview_receiverInfo;
    private LinearLayout linear_newOrderAddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);

        recycview_receiverInfo = findViewById(R.id.recycview_receiverInfo);
        linear_newOrderAddress = findViewById(R.id.linear_newOrderAddress);

        recycview_receiverInfo.setHasFixedSize(true);
        recycview_receiverInfo.setLayoutManager(new LinearLayoutManager(this));

        receiverInfos = new ArrayList<>();

        orderAddressAdapter = new OrderAddressAdapter((Context) this, receiverInfos);

        recycview_receiverInfo.setAdapter(orderAddressAdapter);

        initData();

        linear_newOrderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewOrderAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        // userID = 1
        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("ReceiverInfo").child("1");
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverInfos.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    ReceiverInfo receiverInfo = dataSnapshot.getValue(ReceiverInfo.class);
                    receiverInfos.add(receiverInfo);
                }

                orderAddressAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error:", error.getDetails());
            }
        });
    }
}