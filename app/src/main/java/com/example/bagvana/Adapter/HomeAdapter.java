package com.example.bagvana.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.fragments.HomeFragment;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private final Context context;
    private final ArrayList<Product> productList;

    public HomeAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Product product = productList.get(position);
        if(product == null) {
            return;
        }

        int price = product.getPrice();

        Log.e("Adapter",product.getName());

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.txt_name.setText(product.getName());
        holder.txt_price.setText(String.valueOf(price));
        holder.txt_color.setText(product.getColor());
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_product;
        public TextView txt_name;
        public TextView txt_color;
        public TextView txt_price;
        public HomeViewHolder(@NonNull View itemView) {

            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_color = itemView.findViewById(R.id.txt_color);
            txt_price = itemView.findViewById(R.id.txt_price);
        }
    }

}