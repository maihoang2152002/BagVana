package com.example.bagvana.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bagvana.DTO.Comment;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateDialog extends Dialog {

    private float userRate = 0;
    private Product product;

    public RateDialog(@NonNull Context context) {
        super(context);
    }

    public RateDialog(@NonNull Context context, Product product) {
        super(context);
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

        rateNowBtn.setOnClickListener(v -> {
            String content = contentReview.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Review")
                    .child(product.getProductID());

            // Push new data to the database
//                String data = "Hello, Firebase!";
            DatabaseReference newRef = myRef.push();

            Comment comment = new Comment(newRef.getKey(), content, "1",
                    product.getProductID(), "", userRate, false, "NgocHai");

            newRef.setValue(comment);
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
