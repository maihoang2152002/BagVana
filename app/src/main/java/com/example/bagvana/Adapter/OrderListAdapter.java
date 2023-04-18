package com.example.bagvana.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;

import java.util.ArrayList;
import java.util.Objects;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MainViewHolder> {
    private final Context context;
    private final ArrayList<Order> orderList;
    private final ItemListener itemListener;

    public OrderListAdapter(Context context, ArrayList<Order> orderList, ItemListener itemListener) {
        this.context = context;
        this.orderList = orderList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_order, parent, false);
        return new MainViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order == null) {
            return;
        }
//        for (Product product: order.getItemsOrdered()) {
//            Log.e("order", product.getProductID());
//            Log.e("order", product.getName());
//        }
        holder.orderID.setText("Order #" + order.getOrderID());
        holder.orderDate.setText("Date: " + order.getOrderDate());
        if (Objects.equals(order.getStatus(), "1")) {
            holder.status.setText("Status: Processing");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        } else if (Objects.equals(order.getStatus(), "2")) {
            holder.status.setText("Status: In Delivery");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.blue));
        } else {
            holder.status.setText("Status: Delivered");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        holder.totalPrice.setText("Total price: " + order.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        if (orderList != null) {
            return orderList.size();
        }
        return 0;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        public TextView orderID, orderDate, status, totalPrice;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            orderID = itemView.findViewById(R.id.order_number);
            orderDate = itemView.findViewById(R.id.order_date);
            status = itemView.findViewById(R.id.order_status);
            totalPrice = itemView.findViewById(R.id.order_total);

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }

}