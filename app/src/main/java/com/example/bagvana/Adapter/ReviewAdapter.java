package com.example.bagvana.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagvana.DTO.Comment;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private int mResource;
    private ArrayList<Comment> reviews;

    public ReviewAdapter(Context context, int resource, ArrayList<Comment> reviews) {
        super(context, resource, reviews);
        this.context = context;
        mResource = resource;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the review for this position
        Comment review = reviews.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        }

        // Get references to the views in the list item layout
//        ImageView avatar = convertView.findViewById(R.id.avatarItem);
//        TextView name = convertView.findViewById(R.id.nameItem);
        TextView content;
        content = convertView.findViewById(R.id.contentItem);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        // Set the values for the views using the review data
//        avatar.setImageResource(review.getAvatar());

        TextView name;
        name = convertView.findViewById(R.id.name);
        name.setText(review.getUserName());
        content.setText(review.getContent());
        ratingBar.setRating((float) review.getRating());

        // Return the completed view to render on screen
        return convertView;
    }
}