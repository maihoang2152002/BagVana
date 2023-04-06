package com.example.bagvana.Activity.OrderList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.HomeAdapter;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.Adapter.ReviewAdapter;
import com.example.bagvana.DTO.Comment;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity implements ItemListener {
    private Order curOrder;
    private TextView orderID, orderDate, totalPrice;
    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        curOrder = (Order) getIntent().getSerializableExtra("order");

        orderID = findViewById(R.id.order_number);
        orderDate = findViewById(R.id.order_date);
//        status = itemView.findViewById(R.id.order_status);
//        address = itemView.findViewById(R.id.order_address);
        totalPrice = findViewById(R.id.order_total);

        orderID.setText("Order #" + curOrder.getOrderID());
        orderDate.setText("Date: " + curOrder.getOrderDate());
//        if (curOrder.getStatus() == "1") {
//            status.setText("Status: Processing");
//        } else if (curOrder.getStatus() == "2") {
//            status.setText("Status: In Delivery");
//        } else {
//            status.setText("Status: Delivered");
//        }
        totalPrice.setText("Total price: " + Integer.toString(curOrder.getTotalPrice()));
//        address.setText("Address: " + curOrder.getAddress());

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, curOrder.getItemsOrdered(), this);

        recyclerView.setAdapter(productListAdapter);
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", curOrder.getItemsOrdered().get(position));
        startActivity(intent);
    }
}
