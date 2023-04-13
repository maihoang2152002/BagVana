package com.example.bagvana.DTO;

public class Comment {
    private String content, userID, productID, avatar, reviewID, userName;
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
    public Comment(String reviewID, String content, String userID, String productID,
                   String avatar, double rating, boolean incognito, String userName) {
        this.content = content;
        this.rating = rating;
        this.avatar = avatar;
        this.userID = userID;
        this.incognito = incognito;
        this.reviewID = reviewID;
        this.productID = productID;
        this.userName = userName;
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
        return userName;
    }

    public String getProductID() { return productID; }

    public boolean getIncognito() { return incognito; }

}