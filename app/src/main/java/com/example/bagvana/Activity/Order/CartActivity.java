package com.example.bagvana.Activity.Order;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.CartAdapter;
import com.example.bagvana.DTO.EventBus.BillCostEvent;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Product> productList;
    private CartAdapter cartAdapter;
    private RecyclerView recyclerview_cart;
    private TextView txt_billCost;
    private Button btn_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerview_cart = findViewById(R.id.recycview_cart);
        txt_billCost = findViewById(R.id.txt_billCost);
        btn_order = findViewById(R.id.btn_order);

        recyclerview_cart.setHasFixedSize(true);
        recyclerview_cart.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        cartAdapter = new CartAdapter(this, productList);

        recyclerview_cart.setAdapter(cartAdapter);

        initData();

        calBillCost();

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils._productList.size() != 0) {
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Bạn chưa chọn sản phẩm để mua hàng")
                            .setPositiveButton("Đồng ý", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }

    private void initData() {
        // userID = 1
        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance()
                .getReference("Cart").child(Utils._user.getId());
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                cartAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error:", error.getDetails());
            }
        });
    }

    private void calBillCost() {
        int billCost = 0;
        for(int i = 0; i < Utils._productList.size(); i++) {
            billCost += Utils._productList.get(i).getAmount() * Utils._productList.get(i).getPrice();
        }
        txt_billCost.setText(String.valueOf(billCost));
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void billCostEvent(BillCostEvent event) {
        if(event != null) {
            calBillCost();
        }
    }
}