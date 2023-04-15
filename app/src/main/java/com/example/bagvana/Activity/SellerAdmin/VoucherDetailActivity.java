package com.example.bagvana.Activity.SellerAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoucherDetailActivity extends AppCompatActivity {

    private TextView txt_discountType0;
    private TextView txt_discountType1;
    private TextView txt_freeshipType2;

    private LinearLayout linear_maxValueDiscount;

    private TextView txt_sign;

    private EditText editTxt_name;
    private EditText editTxt_range;
    private EditText editTxt_minValueOfItem;
    private EditText editTxt_maxValueDiscount;
    private EditText editTxt_voucherID;
    private EditText editTxt_amount;

    private TextView txt_amountOnPerson;

    private ImageView imageView_minusAmountOnPerson;
    private ImageView imageView_plusAmountOnPerson;

    private TextView txt_startDate;
    private TextView txt_endDate;

    private ImageView imageView_startDate;
    private  ImageView imageView_endDate;
    private Button btn_apply;

    String voucherID;
    private Voucher voucher;
    private Toolbar toolbar_voucherDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        toolbar_voucherDetail = findViewById(R.id.toolbar_voucherDetail);
        setSupportActionBar(toolbar_voucherDetail);

        // Get voucherID
        Intent intent = getIntent();
        voucherID = intent.getStringExtra("voucherID");


        // Option: giảm giá theo mức, theo %, vận chuyển
        txt_discountType0 = findViewById(R.id.txt_discountType0);
        txt_discountType1 = findViewById(R.id.txt_discountType1);
        txt_freeshipType2 = findViewById(R.id.txt_freeshipType2);

        linear_maxValueDiscount = findViewById(R.id.linear_maxValueDiscount);

        txt_sign = findViewById(R.id.txt_sign);

        editTxt_name = findViewById(R.id.editTxt_name);
        editTxt_range = findViewById(R.id.editTxt_range);
        editTxt_minValueOfItem = findViewById(R.id.editTxt_minValueOfItem);
        editTxt_maxValueDiscount = findViewById(R.id.editTxt_maxValueDiscount);
        editTxt_voucherID = findViewById(R.id.editTxt_voucherID);
        editTxt_amount = findViewById(R.id.editTxt_amount);

        txt_amountOnPerson = findViewById(R.id.txt_amountOnPerson);

        txt_startDate = findViewById(R.id.txt_startDate);
        txt_endDate = findViewById(R.id.txt_endDate);

        initData();
    }

    private void initData() {
        DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher").child(voucherID);
        databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Voucher vou = snapshot.getValue(Voucher.class);
                editTxt_name.setText(vou.getName());
                editTxt_range.setText(String.valueOf(vou.getRange()));
                editTxt_minValueOfItem.setText(String.valueOf(vou.getMinValueOfItem()));
                editTxt_maxValueDiscount.setText(String.valueOf(vou.getMaxValueDiscount()));
                editTxt_amount.setText(String.valueOf(vou.getAmount()));
                txt_amountOnPerson.setText(String.valueOf(vou.getAmountOnPerson()));
                txt_startDate.setText(vou.getStart());
                txt_endDate.setText(vou.getEnd());
                editTxt_voucherID.setText(vou.getId());

                switch (vou.getType()) {
                    case 0:
                        // giảm giá tối đa
                        linear_maxValueDiscount.setVisibility(View.GONE);

                        // sign đ
                        txt_sign.setText("đ");

                        // blue for txt_discountType0
                        txt_discountType0.setTextColor(getResources().getColor(R.color.blue));
                        txt_discountType0.setBackgroundResource(R.drawable.custome_border_shape);

                        //grey for the others
                        txt_discountType1.setTextColor(getResources().getColor(R.color.black));
                        txt_discountType1.setBackgroundResource(R.color.grey);

                        txt_freeshipType2.setTextColor(getResources().getColor(R.color.black));
                        txt_freeshipType2.setBackgroundResource(R.color.grey);
                        break;
                    case 1:
                        // sign đ
                        txt_sign.setText("%");

                        // blue for txt_discountType1
                        txt_discountType1.setTextColor(getResources().getColor(R.color.blue));
                        txt_discountType1.setBackgroundResource(R.drawable.custome_border_shape);
                        //grey for the others
                        txt_discountType0.setTextColor(getResources().getColor(R.color.black));
                        txt_discountType0.setBackgroundResource(R.color.grey);

                        txt_freeshipType2.setTextColor(getResources().getColor(R.color.black));
                        txt_freeshipType2.setBackgroundResource(R.color.grey);
                        break;
                    case 2:
                        // sign đ
                        txt_sign.setText("%");

                        // blue for txt_discountType2
                        txt_freeshipType2.setTextColor(getResources().getColor(R.color.blue));
                        txt_freeshipType2.setBackgroundResource(R.drawable.custome_border_shape);

                        //grey for the others
                        txt_discountType0.setTextColor(getResources().getColor(R.color.black));
                        txt_discountType0.setBackgroundResource(R.color.grey);

                        txt_discountType1.setTextColor(getResources().getColor(R.color.black));
                        txt_discountType1.setBackgroundResource(R.color.grey);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setSupportActionBar(Toolbar toolbar_voucherDetail) {
        toolbar_voucherDetail.setNavigationIcon(R.drawable.ic_back);
        toolbar_voucherDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}