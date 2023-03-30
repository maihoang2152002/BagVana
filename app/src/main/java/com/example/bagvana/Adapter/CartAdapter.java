package com.example.bagvana.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DTO.EventBus.BillCostEvent;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<Product> productList;

    private boolean chooseAll;

    private int billCost;

    public int getBillCost() {
        return billCost;
    }

    public void setBillCost(int billCost) {
        this.billCost = billCost;
    }

    public boolean isChooseAll() {
        return chooseAll;
    }

    public void setChooseAll(boolean chooseAll) {
        this.chooseAll = chooseAll;
    }

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

        final int amount = product.getAmount();
        final int price = product.getPrice();
        final int total = amount * price;

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.txt_name.setText(product.getName());
        holder.txt_amount.setText(String.valueOf(product.getAmount()));
        holder.txt_total.setText(String.valueOf(total));
        holder.txt_color.setText(product.getColor());

        for(int i = 0; i < Utils.productList.size(); i++) {
            if(Utils.productList.get(i).getProductID() == product.getProductID()) {
                holder.checkBox_product.isChecked();
            }
            EventBus.getDefault().postSticky(new BillCostEvent());
        }

        // userID = 1
        DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart").child("1");
        holder.txt_plus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int amountChanged = product.getAmount();
                amountChanged = amountChanged + 1 ;
                holder.txt_amount.setText(String.valueOf(amountChanged));
                product.setAmount(amountChanged); //add to fire-base

                int totalChange = product.getPrice() * amountChanged;
                holder.txt_total.setText(String.valueOf(totalChange));

                databaseReferenceCart.child(product.getProductID()).setValue(product);
                for(int i = 0; i < Utils.productList.size(); i++) {
                    if(Utils.productList.get(i).getProductID() == product.getProductID()) {
                        Utils.productList.get(i).setAmount(amountChanged);
                    }
                    EventBus.getDefault().postSticky(new BillCostEvent());
                }
            }
        });

        holder.txt_minus.setOnClickListener(new View.OnClickListener() {

            DatabaseReference databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart").child("1");
            @Override
            public void onClick(View view) {
                int amountChanged = product.getAmount();
                if (amountChanged == 1) {
                    new AlertDialog.Builder(context)
                            .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReferenceCart.child(product.getProductID()).removeValue();
                                    for(int i = 0; i < Utils.productList.size(); i++) {
                                        if(Utils.productList.get(i).getProductID() == product.getProductID()) {
                                            Utils.productList.remove(i);
                                        }
                                        EventBus.getDefault().postSticky(new BillCostEvent());
                                    }
                                }
                            })

                            .setNegativeButton("Hủy", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                    EventBus.getDefault().postSticky(new BillCostEvent());
                }
                else {
                    amountChanged = amountChanged - 1;
                    holder.txt_amount.setText(String.valueOf(amountChanged));
                    product.setAmount(amountChanged); //add to fire-base

                    int totalChange = product.getPrice() * amountChanged;
                    holder.txt_total.setText(String.valueOf(totalChange));

                    databaseReferenceCart.child(product.getProductID()).setValue(product);
                    for(int i = 0; i < Utils.productList.size(); i++) {
                        if(Utils.productList.get(i).getProductID() == product.getProductID()) {
                            Utils.productList.get(i).setAmount(amountChanged);
                        }
                        EventBus.getDefault().postSticky(new BillCostEvent());
                    }
                }

            }
        });

        holder.checkBox_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Utils.productList.add(product);
                    EventBus.getDefault().postSticky(new BillCostEvent());
                } else {
                    for(int i = 0; i < Utils.productList.size(); i++) {
                        if(Utils.productList.get(i).getProductID() == product.getProductID()) {
                            Utils.productList.remove(i);
                        }
                        EventBus.getDefault().postSticky(new BillCostEvent());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox_product;
        public ImageView img_product;
        public TextView txt_name;
        public TextView txt_color;
        public TextView txt_amount;
        public TextView txt_total;
        public TextView txt_minus;
        public TextView txt_plus;
        public CartViewHolder(@NonNull View itemView) {

            super(itemView);

            checkBox_product = itemView.findViewById(R.id.checkBox_product);
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
