package com.example.bagvana.Adapter;

import static com.example.bagvana.Utils.Utils._user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProductListAdapter extends RecyclerView.Adapter<AdminProductListAdapter.MainViewHolder> {
    private final Context context;
    private final ArrayList<Product> productList;
    private final ItemListener itemListener;

    public AdminProductListAdapter(Context context, ArrayList<Product> productList, ItemListener itemListener) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_single_item_product, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Product product = productList.get(position);
        final boolean[] checkedLocked = {true};
        if (product == null) {
            return;
        }

        Glide.with(this.context)
                .load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_product);

        holder.name.setText(product.getName());
        holder.txt_color.setText("Màu: " + product.getColor());
        holder.txt_price.setText(String.valueOf(product.getPrice()));
        holder.ratingBar.setRating((float) product.getRating());
        holder.txt_amount.setText("Số lượng: " + String.valueOf(product.getAmount()));

        if(product.getStatus().equals("0")){
            holder.btnLocked.setImageResource(R.drawable.ic_lock_person);

        }
        else{
            holder.btnLocked.setImageResource(R.drawable.ic_lock_open);
        }

        holder.btnLocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getStatus().equals("0")){
                    checkedLocked[0] = false;
                    noticeLockedUser(product, holder, checkedLocked[0]);
                }
                else{
                    checkedLocked[0] = true;
                    noticeLockedUser(product, holder, checkedLocked[0]);

                }
            }
        });

    }

    private void noticeLockedUser(Product product, MainViewHolder holder, boolean typeNotice) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // khoa
                if(typeNotice){

                    product.setStatus("0");
                    holder.btnLocked.setImageResource(R.drawable.ic_lock_person);
                }
                else{

                    product.setStatus("1");
                    holder.btnLocked.setImageResource(R.drawable.ic_lock_person);
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
                reference.child(product.getProductID()).setValue(product);

            }
        });
        if(typeNotice){
            alert.setTitle("Khóa sản phẩm!");
            alert.setMessage("Bạn chắc chắn muốn khóa sản phẩm này?");
        }
        else{
            alert.setTitle("Mở khóa sản phẩm!");
            alert.setMessage("Bạn chắc chắn muốn mở khóa sản phẩm này?");
        }

        alert.show();
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
        public TextView name, txt_color, txt_price, txt_amount;
        public RatingBar ratingBar;
        private ImageView btnEdit, btnLocked;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.nameId);
            txt_color = itemView.findViewById(R.id.color_product);
            txt_price = itemView.findViewById(R.id.price_product);
            txt_amount = itemView.findViewById(R.id.amount_product);
            ratingBar = itemView.findViewById((R.id.ratingBar));
            btnEdit = itemView.findViewById((R.id.btnEditProduct));
            btnLocked = itemView.findViewById((R.id.btnLockProduct));

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }

}