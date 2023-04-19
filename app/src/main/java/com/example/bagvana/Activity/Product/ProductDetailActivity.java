package com.example.bagvana.Activity.Product;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.Adapter.HomeAdapter;
import com.example.bagvana.Adapter.ReviewAdapter;
import com.example.bagvana.DTO.Comment;
import com.example.bagvana.DTO.EventBus.BillCostEvent;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity implements ItemListener {
    private ImageView imageSelected, txt_minus, txt_plus, btn_add_to_cart;
    private ImageButton imageBtn_fav, imageBtn_share;
    Product curProduct;
    TextView name, color, price, description, txt_amount;

    ArrayList<Comment> listComment;
    private Toolbar toolbar;

    private RatingBar ratingBar;
    private Button addToCartButton;
    ListView commentListView;

    ReviewAdapter reviewAdapter;
    private HomeAdapter homeAdapter;
    private ArrayList<Product> productList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageBtn_fav = findViewById(R.id.imageBtn_fav); imageBtn_fav.setTag("false");
        imageBtn_share = findViewById(R.id.imageBtn_share);

        imageSelected = findViewById(R.id.img_selected);
        txt_minus = findViewById(R.id.txt_minus);
        txt_plus = findViewById(R.id.txt_plus);
        txt_amount = findViewById(R.id.txt_amount);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);

        name = findViewById(R.id.name_product);
        color = findViewById(R.id.color_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.description_product);

        if (getIntent().hasExtra("product")) {
            curProduct = (Product) getIntent().getSerializableExtra("product");
        }
        else {
            Uri uri = getIntent().getData();
            if (uri != null)
            {
                String path = uri.toString();
                String[] params = path.split("/");
                Log.e("ProductID", params[params.length-1]);

                for (Product t : Utils._productList) {
                    if (params[params.length-1].equals(t.getProductID())) {
                        curProduct = t;
                        break;
                    }
                }
            }
        }


        DatabaseReference databaseReferenceWishlist = FirebaseDatabase.getInstance().getReference("Wishlist/" + _user.getId() + "/List");
        loadFavIconStatus(imageBtn_fav,databaseReferenceWishlist);
        imageBtn_fav.setOnClickListener(v -> {
            if (imageBtn_fav.getTag() == "false"){
                imageBtn_fav.setImageResource(R.drawable.ic_red_fav);
                imageBtn_fav.setTag("true");

                //thêm vào list
                databaseReferenceWishlist.child(String.valueOf(curProduct.getProductID())).setValue(curProduct);
                Toast.makeText(ProductDetailActivity.this, "Add to Favorite successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                imageBtn_fav.setImageResource(R.drawable.ic_fav);
                imageBtn_fav.setTag("false");

                // xóa khỏi list
                databaseReferenceWishlist.child(String.valueOf(curProduct.getProductID())).removeValue();
                Toast.makeText(ProductDetailActivity.this, "Remove from Favorite successfully", Toast.LENGTH_SHORT).show();
            }
        });
        imageBtn_share.setOnClickListener(v -> {
            final String appPakageName = ProductDetailActivity.this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, curProduct.getName() + " : https://example.com/product/" + curProduct.getProductID() );
            sendIntent.setType("text/plain");
            ProductDetailActivity.this.startActivity(sendIntent);
        });
        price.setText("$" + curProduct.getPrice());
        description.setText(curProduct.getDescription());
        name.setText(curProduct.getName());
        color.setText(curProduct.getColor());
        Glide.with(this).load(curProduct.getImage())
                .centerCrop().placeholder(R.drawable.ic_account)
                .into(imageSelected);

        description.setOnClickListener(v -> toggleTextView(description));

        txt_plus.setOnClickListener(view -> {
            int amountChanged = Integer.parseInt(String.valueOf(txt_amount.getText()));
            amountChanged = amountChanged + 1 ;
            txt_amount.setText(String.valueOf(amountChanged));
            curProduct.setAmount(amountChanged);
        });

        txt_minus.setOnClickListener(view -> {
            int amountChanged = Integer.parseInt(String.valueOf(txt_amount.getText()));
            if (amountChanged == 1) {
                return;
            }
            else {
                amountChanged = amountChanged - 1;
                txt_amount.setText(String.valueOf(amountChanged));
                curProduct.setAmount(amountChanged); //add to fire-base
            }
        });

        btn_add_to_cart.setOnClickListener(view -> {
            int amount = Integer.parseInt(String.valueOf(txt_amount.getText()));

            curProduct.setAmount(amount);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Cart")
                    .child(_user.getId());

            // Push new data to the database
//                String data = "Hello, Firebase!";
            myRef.child(curProduct.getProductID()).setValue(curProduct);
        });

        listComment = new ArrayList<>();

        DatabaseReference databaseReferenceReview = FirebaseDatabase.getInstance().getReference("Review/" + curProduct.getProductID());
        databaseReferenceReview.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    listComment.add(comment);
                }

                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reviewAdapter = new ReviewAdapter(this, R.layout.review_product, listComment);
        commentListView = findViewById(R.id.commentListView);
        commentListView.setAdapter(reviewAdapter);

        RecyclerView recyclerview_home = findViewById(R.id.recyclerview_home);

        recyclerview_home.setHasFixedSize(true);
        recyclerview_home.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        productList = new ArrayList<>();

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        homeAdapter = new HomeAdapter(this, productList, this);

        recyclerview_home.setAdapter(homeAdapter);


    }

    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void toggleTextView(TextView textView) {
        if (textView.getMaxLines() == 4) {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
        } else {
            textView.setMaxLines(4);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", productList.get(position));
        startActivity(intent);
    }

    public void loadFavIconStatus(ImageButton imageBtn_fav, DatabaseReference databaseReferenceWishlist){
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

}