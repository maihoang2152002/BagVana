package com.example.bagvana.Adapter;

import static com.example.bagvana.Utils.Utils._user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.Activity.Profile.EditProfileActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MainViewHolder> {
    private final Context context;
    private final ArrayList<Product> productList;
    private final ItemListener itemListener;

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

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.name.setText(product.getName());
        holder.txt_color.setText(product.getColor());
        holder.txt_price.setText(String.valueOf(product.getPrice()));
        holder.ratingBar.setRating((float) product.getRating());
        DatabaseReference databaseReferenceWishlist = FirebaseDatabase.getInstance().getReference("Wishlist/" + _user.getId() + "/List");
        loadFavIconStatus(holder.imageBtn_fav, databaseReferenceWishlist, product);
        holder.imageBtn_fav.setOnClickListener(v -> {
            if (holder.imageBtn_fav.getTag() == "false"){
                holder.imageBtn_fav.setImageResource(R.drawable.ic_red_fav);
                holder.imageBtn_fav.setTag("true");

                //thêm vào list
                databaseReferenceWishlist.child(String.valueOf(product.getProductID())).setValue(product);
                Toast.makeText(context, "Add to Favorite successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                holder.imageBtn_fav.setImageResource(R.drawable.ic_fav);
                holder.imageBtn_fav.setTag("false");

                // xóa khỏi list
                databaseReferenceWishlist.child(String.valueOf(product.getProductID())).removeValue();
                Toast.makeText(context, "Remove from Favorite successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFavIconStatus(ImageButton imageBtn_fav, DatabaseReference databaseReferenceWishlist, Product curProduct) {
        databaseReferenceWishlist.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String productId_Fb = ds.child("productID").getValue(String.class);
                        if(curProduct.getProductID().equals(productId_Fb)) {
                            imageBtn_fav.setTag("true");
                            imageBtn_fav.setImageResource(R.drawable.ic_red_fav);
                            break;
                        }
                    }
                }
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
        public TextView txt_color;
        public TextView txt_price;
        public RatingBar ratingBar;
        public ImageButton imageBtn_fav;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.nameId);
            txt_color = itemView.findViewById(R.id.color_product);
            txt_price = itemView.findViewById(R.id.price_product);
            ratingBar = itemView.findViewById((R.id.ratingBar));
            imageBtn_fav = itemView.findViewById((R.id.imageBtn_fav));
            imageBtn_fav.setTag("false");

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }

}