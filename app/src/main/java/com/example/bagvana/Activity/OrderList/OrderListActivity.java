package com.example.bagvana.Activity.OrderList;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.OrderListAdapter;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity implements ItemListener {

    private RecyclerView recyclerView;
    private OrderListAdapter orderListAdapter;
    private ArrayList<Order> orderList;
    private Toolbar toolbar;
    private String status;
    private TextView textView_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra("title")) {
            toolbar.setTitle(getIntent().getStringExtra("title"));
        }

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();

        status = getIntent().getStringExtra("status");

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceHome.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getStatus().equals(status) && order.getUserID().equals(_user.getId())) {
                        orderList.add(order);
                    }
                }

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
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order", orderList.get(position));
        startActivity(intent);
    }
    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
