package com.example.bagvana.Activity.SellerAdmin;

import static com.example.bagvana.Utils.Utils._user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.bagvana.Activity.OrderList.OrderDetailActivity;
import com.example.bagvana.Adapter.OrderListAdapter;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdminConfirmActivity extends AppCompatActivity implements ItemListener {

    private RecyclerView recyclerView;
    private OrderListAdapter orderListAdapter;
    private ArrayList<Order> orderList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();


        loadOrderList();
    }

    private void loadOrderList() {
        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceHome.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Order order = dataSnapshot.getValue(Order.class);
                        orderList.add(order);
                }
                Collections.sort(orderList, new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        return Integer.compare(Integer.parseInt(o1.getStatus()), Integer.parseInt(o2.getStatus()));
                    }
                });
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        orderListAdapter = new OrderListAdapter(this, orderList, this);
        recyclerView.setAdapter(orderListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK ) {

            loadOrderList();
        }
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order", orderList.get(position));
        startActivityForResult(intent, 1);
    }
    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}