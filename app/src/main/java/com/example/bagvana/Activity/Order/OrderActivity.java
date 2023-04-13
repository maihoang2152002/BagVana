package com.example.bagvana.Activity.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.OrderAdapter;
import com.example.bagvana.DTO.EventBus.VoucherCostEvent;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    private ArrayList<Product> productList;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerview_order;
    private ArrayList<ReceiverInfo> receiverInfo;
    private TextView txt_productCost;
    private TextView txt_shipCost;
    private TextView txt_discountShipCost;
    private TextView txt_voucherCost;
    private TextView txt_totalCost;
    private TextView txt_orderAddress;
    private TextView txt_delivery;
    private TextView txt_billCost;
    private LinearLayout linear_orderAddress;
    private LinearLayout linear_voucher;
    private LinearLayout linear_pay;
    private LinearLayout linear_discountShipCost;
    private LinearLayout linear_voucherCost;
    private Button btn_order;
    private Toolbar toolbar_order;
    private int billCost;
    private int voucherCost;
    private int freeShipCost;
    private int discountCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar_order = findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar_order);

        recyclerview_order = findViewById(R.id.recycview_order);
        txt_productCost = findViewById(R.id.txt_productCost);
        txt_shipCost = findViewById(R.id.txt_shipCost);
        txt_discountShipCost = findViewById(R.id.txt_discountShipCost);
        txt_voucherCost = findViewById(R.id.txt_voucherCost);
        txt_totalCost = findViewById(R.id.txt_totalCost);
        txt_orderAddress = findViewById(R.id.txt_orderAddress);
        txt_delivery = findViewById(R.id.txt_delivery);
        txt_billCost = findViewById(R.id.txt_billCost);

        txt_delivery.setVisibility(View.GONE);

        linear_orderAddress = findViewById(R.id.linear_orderAddress);
        linear_voucher = findViewById(R.id.linear_voucher);
        linear_pay = findViewById(R.id.linear_pay);
        linear_discountShipCost = findViewById(R.id.linear_discountShipCost);
        linear_voucherCost = findViewById(R.id.linear_voucherCost);

        linear_discountShipCost.setVisibility(View.GONE);
        linear_voucherCost.setVisibility(View.GONE);

        btn_order = findViewById(R.id.btn_order);

        if(Utils._receiverInfo.getAddress() != null) {

            String txt_address = Utils._receiverInfo.getFullName() + " | " + Utils._receiverInfo.getPhone() + '\n' + Utils._receiverInfo.getAddress();
            txt_orderAddress.setText(txt_address);
            txt_shipCost.setText("30");

        } else {
            initOrderAddress();
            txt_shipCost.setText("30");
        }


        recyclerview_order.setHasFixedSize(true);
        recyclerview_order.setLayoutManager(new LinearLayoutManager(this));

        productList = (ArrayList<Product>) Utils._productList;

        orderAdapter = new OrderAdapter((Context) this, productList);

        recyclerview_order.setAdapter(orderAdapter);

        linear_orderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, OrderAddressActivity.class);
                startActivity(intent);
            }
        });

        linear_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ChooseVoucherActivity.class);
                startActivity(intent);
            }
        });

        linear_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        completeOrder();

        calBillCost();

        calVoucherCost();
    }

    private void completeOrder() {
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils._receiverInfo.getAddress() == null || txt_delivery.getText().equals("")) {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Bạn chưa điền đầy đủ thông tin địa chỉ hoặc chọn phương thức thanh toán!")
                            .setPositiveButton("Đồng ý", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = localDateTime.format(myFormatObj);

                    DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Order").child(formattedDate);

                    //OrderID
                    databaseReferenceOrder.child(formattedDate);

                    for (Product product: Utils._productList) {
                        databaseReferenceOrder.child("itemsOrdered").child(product.getProductID()).setValue(product);
                    }

                    HashMap<String, Integer> usedVoucher = new HashMap<>();

                    for (Voucher voucher: Utils._voucherList) {
                        if (voucher.getType() == 2) {
                            usedVoucher.put(voucher.getId(), freeShipCost);
                        } else {
                            usedVoucher.put(voucher.getId(), discountCost);
                        }
                        Log.e("voucher", String.valueOf(usedVoucher.get(voucher.getId())));
                    }

                    databaseReferenceOrder.child("usedVoucher").setValue(usedVoucher);


                    // UpdateUserID
                    databaseReferenceOrder.child("orderID").setValue(formattedDate);
                    databaseReferenceOrder.child("receiverInfo").setValue(Utils._receiverInfo);
                    databaseReferenceOrder.child("totalPrice").setValue(Integer.valueOf(txt_totalCost.getText().toString()));
                    databaseReferenceOrder.child("orderDate").setValue(formattedDate);
                    databaseReferenceOrder.child("status").setValue("1");
                    databaseReferenceOrder.child("userID").setValue(Utils._user.getId());
                    databaseReferenceOrder.child("paymentMethod").setValue(txt_delivery.getText().toString());


                }



            }
        });
    }

    private void calVoucherCost() {
        voucherCost = 0;
        for(Voucher voucher: Utils._voucherList) {
            if(voucher.getType() == 2) {

                linear_discountShipCost.setVisibility(View.VISIBLE);

                int discount = billCost * voucher.getRange() / 100;
                if(discount > voucher.getMaxValueDiscount()) {
                    discount = voucher.getMaxValueDiscount();
                    voucherCost =  voucherCost + discount;
                    freeShipCost = discount;
                }
                String money = "-" + discount;
                txt_discountShipCost.setText(money);
            }
            if(voucher.getType() == 0 | voucher.getType() == 1) {
                linear_voucherCost.setVisibility(View.VISIBLE);

                if(voucher.getType() == 0) {

                    int discount = voucher.getRange();
                    voucherCost =  voucherCost + discount;
                    String money = "-" + discount;
                    discountCost = discount;
                    txt_voucherCost.setText(money);
                } else {

                    int discount = billCost * voucher.getRange() / 100;
                    voucherCost =  voucherCost + discount;
                    if(discount > voucher.getMaxValueDiscount()) {
                        discount = voucher.getMaxValueDiscount();
                    }
                    String money = "-" + discount;
                    discountCost = discount;
                    txt_voucherCost.setText(money);
                }
            }

            int totalCost = billCost + 30 - voucherCost;
            txt_totalCost.setText(String.valueOf(totalCost));
            txt_billCost.setText(txt_totalCost.getText().toString());
        }
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_payment_methods_dialog);

        LinearLayout linear_momo_payment_method = bottomSheetDialog.findViewById(R.id.linear_momo_payment_method);
        LinearLayout linear_zalopay_payment_method = bottomSheetDialog.findViewById(R.id.linear_zalopay_payment_method);
        LinearLayout linear_cash_payment_method = bottomSheetDialog.findViewById(R.id.linear_cash_payment_method);

        linear_cash_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_delivery.setVisibility(View.VISIBLE);
                txt_delivery.setText("Thanh toán bằng tiền mặt");

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void setSupportActionBar(Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_back);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initOrderAddress() {
        receiverInfo = new ArrayList<>();

        // userID = 1
        DatabaseReference databaseReferenceReceiverInfo = FirebaseDatabase.getInstance().getReference("ReceiverInfo").child("1");
        databaseReferenceReceiverInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverInfo.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    ReceiverInfo receiverInfo = dataSnapshot.getValue(ReceiverInfo.class);
                    if(Utils._receiverInfo.getFullName() == null && receiverInfo.isDefaultAddress()) {
                        Utils._receiverInfo = receiverInfo;
                        String txt_address = receiverInfo.getFullName() + " | " + receiverInfo.getPhone() + '\n' + receiverInfo.getAddress();
                        txt_orderAddress.setText(txt_address);
                        break;
                    }

                    OrderActivity.this.receiverInfo.add(receiverInfo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error:", error.getDetails());
            }
        });


    }

    private void calBillCost() {
        billCost = 0;
        for(int i = 0; i < Utils._productList.size(); i++) {
            billCost += Utils._productList.get(i).getAmount() * Utils._productList.get(i).getPrice();
        }
        txt_productCost.setText(String.valueOf(billCost));
        int totalCost = billCost + 30 - voucherCost;
        txt_totalCost.setText(String.valueOf(totalCost));
        txt_billCost.setText(txt_totalCost.getText().toString());
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
    public void voucherCostEvent(VoucherCostEvent event) {
        if(event != null) {
            calVoucherCost();
        }
    }
}