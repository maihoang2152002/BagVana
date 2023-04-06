package com.example.bagvana.Activity.OrderList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.OrderListAdapter;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Order order = dataSnapshot.getValue(Order.class);
                    order.getItemsOrdered().remove(0);
                    orderList.add(order);
                    Log.e("test", order.getItemsOrdered().get(1).getName());
                }
//                for (Order order: orderList) {
//                    DatabaseReference databaseReferenceHome2 = FirebaseDatabase.getInstance().getReference("Order").child(order.getOrderID()).child("itemsOrdered");
//                    databaseReferenceHome2.addValueEventListener(new ValueEventListener() {
//                        @SuppressLint("NotifyDataSetChanged")
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            ArrayList<Product> productArrayList = new ArrayList<>();
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                                Product product = dataSnapshot.getValue(Product.class);
//                                productArrayList.add(product);
//                            }
//
//                            order.setItemsOrdered(productArrayList);
//                            Log.e("test", "123");
//                            Log.e("test", order.getItemsOrdered().get(1).getName());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
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
}
