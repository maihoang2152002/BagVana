package com.example.bagvana.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DTO.Notification;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;

import java.io.File;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final ArrayList<Notification> notifications;
    private final ItemListener itemListener;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications, ItemListener itemListener) {
        this.context = context;
        this.notifications = notifications;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        // xử lý
        Glide.with(context)
                .load(notification.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.txt_title.setText(notification.getTitle());
        holder.txt_message.setText(notification.getMessage());
        holder.txt_time.setText(notification.getTime());

        if(notification.getStatus().equals("0")) {
            holder.item_notification.setBackgroundColor(Color.parseColor("#83B5F1"));
        }
    }

    @Override
    public int getItemCount() {
        if(notifications.isEmpty()) {
            return 0;
        }
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_notification;
        private ImageView img_product;
        private TextView txt_title;
        private TextView txt_message;
        private TextView txt_time;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            item_notification = itemView.findViewById(R.id.item_notification);
            img_product = itemView.findViewById(R.id.img_product);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_time = itemView.findViewById(R.id.txt_time);

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }
}
