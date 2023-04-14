package com.example.bagvana.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.SellerAdmin.VoucherDetailActivity;
import com.example.bagvana.DAO.VoucherDAO;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private Context context;
    private ArrayList<Voucher> vouchers;

    public VoucherAdapter(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher,parent,false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);

        holder.txt_name.setText(voucher.getName());

        String date = voucher.getStart() + " - " + voucher.getEnd();
        holder.txt_date.setText(date);

        String range;

        if(voucher.getType() == 0) {
            range = "đ" +  String.valueOf(voucher.getRange());
        } else {
            range = String.valueOf(voucher.getRange()) + "%";
        }

        holder.txt_range.setText(range);

        String minValueOfItem = "Đơn tối thiểu đ" + voucher.getMinValueOfItem();

        holder.txt_minValueOfItem.setText(minValueOfItem);

        // Đã sử dụng => duyệt qua order
        DatabaseReference databaseReferenceVoucher = FirebaseDatabase.getInstance().getReference("Voucher").child(voucher.getId());

        VoucherDAO voucherDAO = new VoucherDAO();
        voucherDAO.readCountUsedVoucher(voucher, new VoucherDAO.MyCallback() {
            @Override
            public void onCallback(String voucherID, int value) {
                holder.txt_usedVoucher.setText(String.valueOf(value));

                databaseReferenceVoucher.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Voucher vou = snapshot.getValue(Voucher.class);
                        int amount = vou.getAmount();
                        int newAmount = amount - value;

                        holder.txt_savedVoucher.setText(String.valueOf(newAmount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        // tình trạng
        try {
            switch (compareDate(voucher.getStart(),voucher.getEnd())) {
                case 1:
                    holder.txt_description.setText("Sắp diễn ra");
                    break;
                case 0:
                    holder.txt_description.setText("Đang diễn ra");
                    break;
                case -1:
                    holder.txt_description.setText("Đã kết thúc");
                    holder.btn_end.setVisibility(View.GONE);
                    break;
                default:
                    break;
        }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Xem, kết thúc
        holder.btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Date date;

                try {
                    date = sdf.parse(voucher.getEnd());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);

                Date newDate = c.getTime();
                Log.e("NewDate", newDate.toString());

                int mDay = c.get(Calendar.DATE);
                int mMonth = c.get(Calendar.MONTH);
                int mYear = c.get(Calendar.YEAR);

                String strDate = mDay + "/" + mMonth + "/" + mYear;

                databaseReferenceVoucher.child("end").setValue(strDate);
            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context, VoucherDetailActivity.class);
                intent.putExtra("voucherID", voucher.getId());
                context.startActivity(intent);
            }
        });
    }

    // sắp diễn ra, đang diễn ra, đã kết thúc
    public int compareDate(String start, String end) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date strDateEnd = sdf.parse(end);
        Date strDateStart = sdf.parse(start);

        // chưa xảy ra
        if (new Date().before(strDateStart) && new Date().before(strDateEnd)) {
            return 1;
        }

        // đã kết thúc
        if (new Date().after(strDateStart) && new Date().after(strDateEnd)) {
            return -1;
        }

        return 0;
    }

    @Override
    public int getItemCount() {

        if(vouchers.size() == 0) {
            return 0;
        }

        return vouchers.size();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_date;
        private TextView txt_range;
        private TextView txt_minValueOfItem;
        private TextView txt_description;
        private TextView txt_usedVoucher;
        private TextView txt_savedVoucher;
        private Button btn_detail;
        private Button btn_end;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_range = itemView.findViewById(R.id.txt_range);
            txt_minValueOfItem = itemView.findViewById(R.id.txt_minValueOfItem);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_usedVoucher = itemView.findViewById(R.id.txt_usedVoucher);
            txt_savedVoucher = itemView.findViewById(R.id.txt_savedVoucher);

            btn_detail = itemView.findViewById(R.id.btn_detail);
            btn_end = itemView.findViewById(R.id.btn_end);

        }
    }
}
