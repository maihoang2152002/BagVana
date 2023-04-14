package com.example.bagvana.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DiscountShopAdapter extends RecyclerView.Adapter<DiscountShopAdapter.DiscountShopViewHolder> {

    private Context context;
    private ArrayList<Voucher> vouchers;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DiscountShopAdapter(Context context, ArrayList<Voucher> vouchers, String type) {
        this.context = context;
        this.vouchers = vouchers;
        this.type = type;
    }

    @NonNull
    @Override
    public DiscountShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_discount_voucher,parent,false);
        return new DiscountShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountShopViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);

        String name;
        String description;
        if(voucher.getType() == 0) {
            name = "Giảm đ" + voucher.getRange() +"k";
            description = "Đơn tối thiểu đ" + voucher.getMinValueOfItem() + "k";
        } else {
            name = "Giảm " + voucher.getRange() + "%";
            description = "Đơn tối thiểu đ" + voucher.getMinValueOfItem() + "k Giảm tối đa đ" + voucher.getMaxValueDiscount() + "k";
        }

        holder.txt_name.setText(name);
        holder.txt_description.setText(description);
        holder.txt_endDate.setText(voucher.getEnd());

        if(Utils._vouchersOfUser.get(voucher.getId()) != null && (Utils._vouchersOfUser.get(voucher.getId()) >= voucher.getAmountOnPerson())) {
            holder.btn_choose.setText("Sử dụng");
        }

        if(this.type.equals("user")) {
            holder.btn_choose.setText("Sử dụng");
        } else {
            holder.btn_choose.setText("Lưu");
        }

        holder.btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_choose.getText().equals("Lưu")) {
                    DatabaseReference databaseReferenceUser_Voucher = FirebaseDatabase.getInstance().getReference("User_Voucher").child("1");
                    int amount = 0;

                    if(Utils._vouchersOfUser.get(voucher.getId()) != null) {
                        amount = Utils._vouchersOfUser.get(voucher.getId());
                    }
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

    public class DiscountShopViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_description;
        private TextView txt_endDate;
        private Button btn_choose;

        public DiscountShopViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_endDate = itemView.findViewById(R.id.txt_endDate);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
