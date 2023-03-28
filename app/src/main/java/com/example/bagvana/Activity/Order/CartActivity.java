package com.example.bagvana.Activity.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bagvana.Adapter.CartAdapter;
import com.example.bagvana.DAO.CartDAO;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartDAO cartDAO;
    private ArrayList<Product> productList;
    private CartAdapter cartAdapter;
    private RecyclerView recycview_cart;
    private TextView txt_billCost;
    private RadioButton rad_chooseAll;

    String billCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycview_cart = findViewById(R.id.recycview_cart);
        txt_billCost = findViewById(R.id.txt_billCost);
        rad_chooseAll = findViewById(R.id.rad_chooseAll);


        recycview_cart.setHasFixedSize(true);
        recycview_cart.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        cartAdapter = new CartAdapter(this, productList);

        //txt_billCost.setText("billCost");

        rad_chooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rad_chooseAll.isSelected()) {
                    rad_chooseAll.setChecked(true);
                    rad_chooseAll.setSelected(true);

                    cartAdapter.setChooseAll(true);
                } else {
                    rad_chooseAll.setChecked(false);
                    rad_chooseAll.setSelected(false);

                    cartAdapter.setChooseAll(false);
                }
            }
        });

        recycview_cart.setAdapter(cartAdapter);

        // userID = 1
        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart").child("1");
        databaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                    Log.e("Actitvity",product.getName());
                }

                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}