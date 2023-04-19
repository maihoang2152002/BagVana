package com.example.bagvana.Activity.Dialog;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bagvana.DTO.Comment;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RateDialog extends Dialog {

    private float userRate = 0;
    private Product product;
    private Order order;

    public RateDialog(@NonNull Context context) {
        super(context);
    }

    public RateDialog(@NonNull Context context, Product product, Order order) {
        super(context);
        this.order = order;
        this.product = product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rate_dialog);

        final AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
        final AppCompatButton laterBtn = findViewById(R.id.laterBtn);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final ImageView ratingImage = findViewById(R.id.ratingImage);
        final EditText contentReview = findViewById(R.id.content);
        final TextView review1 = findViewById(R.id.review1);
        final TextView review2 = findViewById(R.id.review2);
        final TextView review3 = findViewById(R.id.review3);
        final TextView review4= findViewById(R.id.review4);

        review1.setOnClickListener(v -> {
            review1.setBackgroundResource(R.drawable.ic_cat_bg_2);
            review2.setBackgroundResource(R.drawable.ic_cat_bg);
            review3.setBackgroundResource(R.drawable.ic_cat_bg);
            review4.setBackgroundResource(R.drawable.ic_cat_bg);
            contentReview.setText(review1.getText());
        });
        review2.setOnClickListener(v -> {
            review2.setBackgroundResource(R.drawable.ic_cat_bg_2);
            review1.setBackgroundResource(R.drawable.ic_cat_bg);
            review3.setBackgroundResource(R.drawable.ic_cat_bg);
            review4.setBackgroundResource(R.drawable.ic_cat_bg);
            contentReview.setText(review2.getText());
        });
        review3.setOnClickListener(v -> {
            review3.setBackgroundResource(R.drawable.ic_cat_bg_2);
            review2.setBackgroundResource(R.drawable.ic_cat_bg);
            review1.setBackgroundResource(R.drawable.ic_cat_bg);
            review4.setBackgroundResource(R.drawable.ic_cat_bg);
            contentReview.setText(review3.getText());
        });
        review4.setOnClickListener(v -> {
            review4.setBackgroundResource(R.drawable.ic_cat_bg_2);
            review2.setBackgroundResource(R.drawable.ic_cat_bg);
            review3.setBackgroundResource(R.drawable.ic_cat_bg);
            review1.setBackgroundResource(R.drawable.ic_cat_bg);
            contentReview.setText(review4.getText());
        });

        rateNowBtn.setOnClickListener(v -> {
            String content = contentReview.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Review")
                    .child(product.getProductID());

            // Push new data to the database
//                String data = "Hello, Firebase!";
            DatabaseReference newRef = myRef.push();

            Comment comment = new Comment(newRef.getKey(), content, _user.getId(),
                    product.getProductID(), "", userRate, false, _user.getUsername());

            newRef.setValue(comment);

            DatabaseReference myRefProduct = database.getReference("Product")
                    .child(product.getProductID());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double temp = 0.0;
                    int count = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        temp += comment.getRating();
                        count++;
                    }
                    myRefProduct.child("rating").setValue(temp / count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference myRefProductOrder = database.getReference("Order").child(order.getOrderID())
                    .child("itemsOrdered").child(product.getProductID()).child("rated");

            myRefProductOrder.setValue("true");

            dismiss();
        });

        laterBtn.setOnClickListener(v -> dismiss());

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (rating <= 1) {
                ratingImage.setImageResource(R.drawable.ic_1_star);
            } else if (rating <= 2) {
                ratingImage.setImageResource(R.drawable.ic_2_star);
            } else if (rating <= 3) {
                ratingImage.setImageResource(R.drawable.ic_3_star);
            } else if (rating <= 4) {
                ratingImage.setImageResource(R.drawable.ic_4_star);
            } else {
                ratingImage.setImageResource(R.drawable.ic_5_star);
            }

            animateImage(ratingImage);
            userRate = rating;
        });
    }

    private void animateImage(ImageView ratingImage) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }
}
