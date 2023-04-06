package com.example.bagvana.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Order.OrderActivity;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;

import java.util.ArrayList;

public class OrderAddressAdapter extends RecyclerView.Adapter<OrderAddressAdapter.OrderAddressViewHolder> {

    private Context context;
    private ArrayList<ReceiverInfo> receiverInfos;

    public OrderAddressAdapter(Context context, ArrayList<ReceiverInfo> receiverInfos) {
        this.context = context;
        this.receiverInfos = receiverInfos;
    }

    @NonNull
    @Override
    public OrderAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_address,parent,false);
        return new OrderAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAddressViewHolder holder, int position) {
        ReceiverInfo receiverInfo = receiverInfos.get(position);
        if(receiverInfo == null) {
            return;
        }

        if(!receiverInfo.isDefaultAddress()) {
            holder.txt_defaultAddress.setVisibility(View.INVISIBLE);
        }

        if(Utils.receiverInfo.getAddressID() == receiverInfo.getAddressID()) {
            holder.rad_address.setChecked(true);
        }

        holder.txt_fullName.setText(receiverInfo.getFullName());
        holder.txt_phone.setText(receiverInfo.getPhone());
        holder.txt_address.setText(receiverInfo.getAddress());

        holder.rad_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.receiverInfo = receiverInfo;

                context.startActivity(new Intent(context, OrderActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(receiverInfos != null) {
            return receiverInfos.size();
        }
        return 0;
    }

    public class OrderAddressViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rad_address;
        private TextView txt_fullName;
        private TextView txt_address;
        private TextView txt_phone;
        private TextView txt_edit;
        private TextView txt_defaultAddress;

        public OrderAddressViewHolder(@NonNull View itemView) {
            super(itemView);

            rad_address = itemView.findViewById(R.id.rad_address);
            txt_fullName = itemView.findViewById(R.id.txt_fullName);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_phone = itemView.findViewById(R.id.txt_phone);
            txt_edit = itemView.findViewById(R.id.txt_edit);
            txt_defaultAddress = itemView.findViewById(R.id.txt_defaultAddress);
        }
    }
}
