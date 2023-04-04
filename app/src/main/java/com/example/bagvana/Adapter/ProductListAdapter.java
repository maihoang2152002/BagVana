package com.example.bagvana.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MainViewHolder> {
    private final Context context;
    private final ArrayList<Product> productList;
    private ItemListener itemListener;

    public ProductListAdapter(Context context, ArrayList<Product> productList, ItemListener itemListener) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        int price = product.getPrice();

        Log.e("Adapter", product.getName());

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.name.setText(product.getName());
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
//        public TextView txt_color;
//        public TextView txt_price;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.nameId);
//            txt_color = itemView.findViewById(R.id.txt_color);
//            txt_price = itemView.findViewById(R.id.txt_price);

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }

}