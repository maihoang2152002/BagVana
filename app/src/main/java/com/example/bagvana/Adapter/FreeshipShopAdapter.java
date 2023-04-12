package com.example.bagvana.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FreeshipShopAdapter extends RecyclerView.Adapter<FreeshipShopAdapter.FreeshipShopViewHolder> {

    private Context context;
    private ArrayList<Voucher> vouchers;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FreeshipShopAdapter(Context context, ArrayList<Voucher> vouchers, String type) {
        this.context = context;
        this.vouchers = vouchers;
        this.type = type;
    }

    @NonNull
    @Override
    public FreeshipShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_freeship_voucher,parent,false);
        return new FreeshipShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeshipShopViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);

        String description = voucher.getRange() + "% - Tối đa " + voucher.getMaxValueDiscount() + "k";
        String minValueOfItem = "Đơn tối thiểu đ" + voucher.getMinValueOfItem() + "k";

        holder.txt_name.setText("Miễn phí vận chuyển");
        holder.txt_minValueOfItem.setText(minValueOfItem);
        holder.txt_description.setText(description);
        holder.txt_endDate.setText(voucher.getEnd());

        if(this.type.equals("user") || (Utils._vouchersOfUser.get(voucher.getId()) >= voucher.getAmountOnPerson())) {
            holder.btn_choose.setText("Sử dụng");
        } else {
            holder.btn_choose.setText("Lưu");
        }

        holder.btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_choose.getText().equals("Lưu")) {
                    DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child("1");
                    int amount = Utils._vouchersOfUser.get(voucher.getId());
                    int newAmount = amount + 1;
                    databaseReferenceUser_Voucher.child(voucher.getId()).child("amount").setValue(newAmount);

                    if(newAmount >= voucher.getAmountOnPerson()) {
                        holder.btn_choose.setText("Sử dụng");
                    }

                } else {
                    // intent qua trang mua hàng
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(vouchers == null) {
            return 0;
        }

        return vouchers.size();
    }

    public class FreeshipShopViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_description;
        private TextView txt_minValueOfItem;
        private TextView txt_endDate;
        private Button btn_choose;

        public FreeshipShopViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_minValueOfItem = itemView.findViewById(R.id.txt_minValueOfItem);
            txt_endDate = itemView.findViewById(R.id.txt_endDate);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
