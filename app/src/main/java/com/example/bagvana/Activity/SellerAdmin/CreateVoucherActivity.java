package com.example.bagvana.Activity.SellerAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.example.bagvana.DAO.VoucherDAO;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateVoucherActivity extends AppCompatActivity {

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
    private Toolbar toolbar_createVoucher;

    // Khởi tạo biến gán cho voucher
    private Voucher voucher;
    private int type = 0;
    private String id;
    private String name;
    private String start;
    private String end;
    private String maxValueDiscount;
    private String minValueOfItem;
    private String range;
    private String amount;
    private String amountOnPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        toolbar_createVoucher = findViewById(R.id.toolbar_createVoucher);
        setSupportActionBar(toolbar_createVoucher);

        // Option: giảm giá theo mức, theo %, vận chuyển
        txt_discountType0 = findViewById(R.id.txt_discountType0);
        txt_discountType1 = findViewById(R.id.txt_discountType1);
        txt_freeshipType2 = findViewById(R.id.txt_freeshipType2);

        linear_maxValueDiscount = findViewById(R.id.linear_maxValueDiscount);
        linear_maxValueDiscount.setVisibility(View.GONE);

        txt_sign = findViewById(R.id.txt_sign);

        editTxt_name = findViewById(R.id.editTxt_name);
        editTxt_range = findViewById(R.id.editTxt_range);
        editTxt_minValueOfItem = findViewById(R.id.editTxt_minValueOfItem);
        editTxt_maxValueDiscount = findViewById(R.id.editTxt_maxValueDiscount);
        editTxt_voucherID = findViewById(R.id.editTxt_voucherID);
        editTxt_amount = findViewById(R.id.editTxt_amount);

        txt_amountOnPerson = findViewById(R.id.txt_amountOnPerson);

        imageView_minusAmountOnPerson = findViewById(R.id.imageView_minusAmountOnPerson);
        imageView_plusAmountOnPerson = findViewById(R.id.imageView_plusAmountOnPerson);

        txt_startDate = findViewById(R.id.txt_startDate);
        txt_endDate = findViewById(R.id.txt_endDate);

        imageView_startDate = findViewById(R.id.imageView_startDate);
        imageView_endDate = findViewById(R.id.imageView_endDate);

        btn_apply = findViewById(R.id.btn_apply);

        setCurrentDate();

        chooseOption();

        PickerAmountOnPerson();

        chooseDate();

        complete();
    }

    private void PickerAmountOnPerson() {
        imageView_minusAmountOnPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt((txt_amountOnPerson.getText().toString()));
                if(amount > 1) {
                    txt_amountOnPerson.setText(String.valueOf(amount - 1));
                }
            }
        });

        imageView_plusAmountOnPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt((txt_amountOnPerson.getText().toString()));
                txt_amountOnPerson.setText(String.valueOf(amount + 1));
            }
        });
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String date = mDay + "/" + (mMonth + 1) + "/" + mYear;

        txt_startDate.setText(date);
        txt_endDate.setText(date);

    }
    private void chooseDate() {
        imageView_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateVoucherActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                txt_startDate.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        imageView_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateVoucherActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                                txt_endDate.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
    }
    private void setColorForOption() {
        switch (type) {
            case 0:
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
    private void chooseOption() {
        txt_discountType0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                linear_maxValueDiscount.setVisibility(View.GONE);

                setColorForOption();
            }
        });

        txt_discountType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                linear_maxValueDiscount.setVisibility(View.VISIBLE);

                setColorForOption();
            }
        });

        txt_freeshipType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                linear_maxValueDiscount.setVisibility(View.VISIBLE);

                setColorForOption();
            }
        });
    }
    private void complete() {
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoucherDAO voucherDAO = new VoucherDAO();
                id = editTxt_voucherID.getText().toString();
                name = editTxt_name.getText().toString();
                start = txt_startDate.getText().toString();
                end = txt_endDate.getText().toString();
                maxValueDiscount = editTxt_maxValueDiscount.getText().toString();
                minValueOfItem = editTxt_minValueOfItem.getText().toString();
                range = editTxt_range.getText().toString();
                amount = editTxt_amount.getText().toString();
                amountOnPerson = txt_amountOnPerson.getText().toString();

                DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher");

                if(id.isEmpty() || name.isEmpty() || start.isEmpty() || end.isEmpty() ||
                        minValueOfItem.isEmpty() || range.isEmpty() || amount.isEmpty() || amountOnPerson.isEmpty()) {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Voucher của bạn tạo không hợp lệ")
                            .setPositiveButton("Đồng ý", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    if(type == 0) {
                        // create voucher
                        Voucher vou = new Voucher(id,name,type,start, end,Integer.parseInt(range),Integer.parseInt(minValueOfItem),Integer.parseInt(range),Integer.parseInt(amount),Integer.parseInt(amountOnPerson));

                        //kiểm tra tồn tại;
                        if(voucherDAO.isExisted(vou)) {
                            new AlertDialog.Builder(view.getContext())
                                    .setMessage("Mã giảm giá này đã tồn tại!")
                                    .setPositiveButton("Đồng ý", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            voucherDAO.addValue(vou);
                        }

                    } else {
                        if(maxValueDiscount.isEmpty()) {
                            new AlertDialog.Builder(view.getContext())
                                    .setMessage("Voucher của bạn tạo không hợp lệ")
                                    .setPositiveButton("Đồng ý", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            // create voucher
                            Voucher vou = new Voucher(id,name,type,start, end,Integer.parseInt(maxValueDiscount),Integer.parseInt(minValueOfItem),Integer.parseInt(range),Integer.parseInt(amount),Integer.parseInt(amountOnPerson));

                            //kiểm tra tồn tại;
                            if(voucherDAO.isExisted(vou)) {
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Mã giảm giá này đã tồn tại!")
                                        .setPositiveButton("Đồng ý", null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                voucherDAO.addValue(vou);
                            }
                        }
                    }
                }

            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar_createVoucher) {
        toolbar_createVoucher.setNavigationIcon(R.drawable.ic_back);
        toolbar_createVoucher.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}