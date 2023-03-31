package com.example.bagvana.DTO;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.bagvana.Adapter.ReviewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Comment {
    private String content, userID, productID, avatar, reviewID, username;
    private double rating;
    boolean incognito;

    public Comment() {
        this.content = "";
        this.userID = "";
        this.rating = 0;
        this.avatar = "";
        incognito = false;
        reviewID = "";
        productID = "";
    }
    public Comment(String reviewID, String content, String userID, String prodductID, String avatar,double rating, boolean incognito) {
        this.content = content;
        this.rating = rating;
        this.avatar = avatar;
        this.userID = userID;
        this.incognito = incognito;
        this.reviewID = reviewID;
        this.productID = prodductID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUserID() {return userID; }

    public String getReviewID() {
        return reviewID;
    }

    public String getUserName() {
        return username;
    }

}