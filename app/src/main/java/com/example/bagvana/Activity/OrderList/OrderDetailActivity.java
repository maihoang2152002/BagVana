package com.example.bagvana.Activity.OrderList;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Activity.SellerAdmin.AdminConfirmActivity;
import com.example.bagvana.Adapter.PListOrderAdapter;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class OrderDetailActivity extends AppCompatActivity implements ItemListener {
    private Order curOrder;
    private TextView orderID, orderDate, totalPrice, address, status;
    private PListOrderAdapter pListOrderAdapter;
    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Button btn_confirm;
    ArrayList<Product> productArrayList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        curOrder = (Order) getIntent().getSerializableExtra("order");
//        Log.e("OrderDate", curOrder.getOrderDate());

        orderID = findViewById(R.id.order_number);
        orderDate = findViewById(R.id.order_date);
        status = findViewById(R.id.order_status);
        address = findViewById(R.id.order_address);
        totalPrice = findViewById(R.id.order_total);


        orderID.setText("Đơn Hàng: #" + curOrder.getOrderID());
        orderDate.setText("Ngày: " + curOrder.getOrderDate());

        totalPrice.setText("Tổng Giá: " + curOrder.getTotalPrice());
        address.setText("Địa Chỉ: " + curOrder.getReceiverInfo().getAddress());

        recyclerView = findViewById(R.id.recyclerviewId);

        productArrayList = new ArrayList<>();
        Set<String> keySet = curOrder.getItemsOrdered().keySet();
        for (String key : keySet) {
            Product temp = curOrder.getItemsOrdered().get(key);
            productArrayList.add(temp);
        }

        if (curOrder.getStatus().equals("1")) {
            status.setText("Trạng Thái: Đang Xử Lý");
            status.setTextColor(getResources().getColor(R.color.yellow));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productListAdapter = new ProductListAdapter(this, productArrayList, this);

            recyclerView.setAdapter(productListAdapter);
        } else if (curOrder.getStatus().equals("2")) {
            status.setText("Trạng Thái: Đang Vận Chuyển");
            status.setTextColor(getResources().getColor(R.color.blue));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productListAdapter = new ProductListAdapter(this, productArrayList, this);

            recyclerView.setAdapter(productListAdapter);
        } else {
            status.setText("Trạng Thái: Đã Giao");
            status.setTextColor(getResources().getColor(R.color.green));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            pListOrderAdapter = new PListOrderAdapter(this, productArrayList, this, curOrder);

            recyclerView.setAdapter(pListOrderAdapter);
        }


        btn_confirm = findViewById(R.id.btn_confirm);
        if ( (Objects.equals(_user.getTypeUser(), "2"))) {
            if (!Objects.equals(curOrder.getStatus(), "1")) {
                btn_confirm.setVisibility(View.GONE);
            }
        } else if ((Objects.equals(_user.getTypeUser(), "1"))) {
            if (!Objects.equals(curOrder.getStatus(), "2")) {
                btn_confirm.setVisibility(View.GONE);
            } else {
                btn_confirm.setText("Received");
            }
        }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_confirm.setVisibility(View.GONE);

                if ( (Objects.equals(_user.getTypeUser(), "2"))) {
                    FirebaseDatabase.getInstance().getReference("Order").child(curOrder.getOrderDate()).child("status")
                            .setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(OrderDetailActivity.this, "Confirm order successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OrderDetailActivity.this, AdminConfirmActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if ((Objects.equals(_user.getTypeUser(), "1"))) {
                    FirebaseDatabase.getInstance().getReference("Order").child(curOrder.getOrderDate()).child("status")
                            .setValue("3").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(OrderDetailActivity.this, "Confirm received successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OrderDetailActivity.this, OrderListActivity.class);
                                        intent.putExtra("status", "2");
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }



            }
        });

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
