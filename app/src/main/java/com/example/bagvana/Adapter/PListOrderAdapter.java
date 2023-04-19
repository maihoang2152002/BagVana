package com.example.bagvana.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.Activity.Dialog.RateDialog;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PListOrderAdapter extends RecyclerView.Adapter<PListOrderAdapter.MainViewHolder> {
    private final Context context;
    private final ArrayList<Product> productList;
    private final ItemListener itemListener;

    private final Order order;

    public PListOrderAdapter(Context context, ArrayList<Product> productList, ItemListener itemListener) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
        order = null;
    }

    public PListOrderAdapter(Context context, ArrayList<Product> productList, ItemListener itemListener, Order order) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
        this.order = order;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_delivered, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.name.setText(product.getName());
        holder.txt_color.setText(product.getColor());
        holder.txt_price.setText(String.valueOf(product.getPrice()));

        holder.product = product;
        holder.order = order;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefProductOrder = database.getReference("Order").child(order.getOrderID())
                .child("itemsOrdered").child(product.getProductID()).child("rated");

        myRefProductOrder.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rated = (String) snapshot.getValue();
                if (rated != null && rated.equals("true")) {
                    holder.btn_rating.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView img_product;
        public TextView name;
        public FloatingActionButton btn_rating;
        public Product product;
        public Order order;
        public TextView txt_color;
        public TextView txt_price;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.nameId);
            btn_rating = itemView.findViewById(R.id.btn_rating);

            txt_color = itemView.findViewById(R.id.color_product);
            txt_price = itemView.findViewById(R.id.price_product);

            btn_rating.setOnClickListener(v -> {
                RateDialog rateDialog = new RateDialog(v.getContext(), product, order);
                rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
                rateDialog.setCancelable(false);
                rateDialog.show();
            });

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }

}