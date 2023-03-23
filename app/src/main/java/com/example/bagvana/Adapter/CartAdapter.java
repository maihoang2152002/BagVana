package com.example.bagvana.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DAO.CartDAO;
import com.example.bagvana.DTO.Cart;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;

import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<Product> productList;

    public CartAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        if(product == null) {
            return;
        }

        int amount = product.getAmount();
        int price = product.getPrice();
        int total = amount * price;

        Log.e("Adapter",product.getName());

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.txt_name.setText(product.getName());
        holder.txt_amount.setText(String.valueOf(product.getAmount()));
        holder.txt_total.setText(String.valueOf(total));
        holder.txt_color.setText(product.getColor());
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public RadioButton rad_product;
        public ImageView img_product;
        public TextView txt_name;
        public TextView txt_color;
        public TextView txt_amount;
        public TextView txt_total;
        public TextView txt_minus;
        public TextView txt_plus;
        public CartViewHolder(@NonNull View itemView) {

            super(itemView);

            rad_product = itemView.findViewById(R.id.rad_product);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_color = itemView.findViewById(R.id.txt_color);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_total = itemView.findViewById(R.id.txt_total);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txt_plus = itemView.findViewById(R.id.txt_plus);

        }
    }

}
