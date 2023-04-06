package com.example.bagvana.Activity.OrderList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;

import java.util.ArrayList;
import java.util.Set;

public class OrderDetailActivity extends AppCompatActivity implements ItemListener {
    private Order curOrder;
    private TextView orderID, orderDate, totalPrice, address;
    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    ArrayList<Product> productArrayList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        curOrder = (Order) getIntent().getSerializableExtra("order");

        orderID = findViewById(R.id.order_number);
        orderDate = findViewById(R.id.order_date);
//        status = findViewById(R.id.order_status);
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
        totalPrice.setText("Total price: " + curOrder.getTotalPrice());
        address.setText("Address: " + curOrder.getReceiverInfo().getAddress());

        recyclerView = findViewById(R.id.recyclerviewId);

        productArrayList = new ArrayList<>();
        Set<String> keySet = curOrder.getItemsOrdered().keySet();
        for (String key : keySet) {
            Product temp = curOrder.getItemsOrdered().get(key);
            productArrayList.add(temp);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, productArrayList, this);

        recyclerView.setAdapter(productListAdapter);
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", productArrayList.get(position));
        startActivity(intent);
    }
    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
