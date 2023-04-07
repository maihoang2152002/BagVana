package com.example.bagvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.DTO.EventBus.VoucherCostEvent;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FreeshipVoucherAdapter extends RecyclerView.Adapter<FreeshipVoucherAdapter.FreeshipVoucherViewHodler> {

    private Context context;
    private ArrayList<Voucher> vouchers;
    private int lastCheckedPosition = -1;
    private boolean checked = false;

    public FreeshipVoucherAdapter(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
    }

    @NonNull
    @Override
    public FreeshipVoucherViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_freeship_voucher,parent,false);
        return new FreeshipVoucherViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeshipVoucherViewHodler holder, int position) {
        Voucher voucher = vouchers.get(position);

        String description = voucher.getRange() + "% - Tối đa " + voucher.getMaxValueDiscount() + "k";
        String minValueOfItem = "Đơn tối thiểu đ" + voucher.getMinValueOfItem() + "k";

        holder.txt_name.setText("Miễn phí vận chuyển");
        holder.txt_minValueOfItem.setText(minValueOfItem);
        holder.txt_description.setText(description);
        holder.txt_endDate.setText(voucher.getEnd());

        if(checked == false) {
            for(Voucher vou: Utils._voucherList) {
                if(vou.getId() == voucher.getId()) {
                    lastCheckedPosition = holder.getAdapterPosition();
                    checked = true;
                }
            }
        }


        holder.rad_choose.setChecked(position == lastCheckedPosition);

    }

    @Override
    public int getItemCount() {
        if(vouchers == null) {
            return 0;
        }

        return vouchers.size();
    }

    public class FreeshipVoucherViewHodler extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_minValueOfItem;
        private TextView txt_description;
        private TextView txt_endDate;
        private RadioButton rad_choose;

        public FreeshipVoucherViewHodler(@NonNull View itemView) {

            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_minValueOfItem = itemView.findViewById(R.id.txt_minValueOfItem);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_endDate = itemView.findViewById(R.id.txt_endDate);
            rad_choose = itemView.findViewById(R.id.rad_choose);

            rad_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                    boolean isExist = false;
                    for(Voucher voucher: Utils._voucherList) {
                        if(voucher.getType() == 2) {
                            isExist = true;
                            Utils._voucherList.remove(voucher);
                            Utils._voucherList.add(vouchers.get(lastCheckedPosition));
                        }
                    }

                    if(isExist == false) {
                        Utils._voucherList.add(vouchers.get(lastCheckedPosition));
                    }

                    EventBus.getDefault().postSticky(new VoucherCostEvent());

                }
            });

        }
    }
}
