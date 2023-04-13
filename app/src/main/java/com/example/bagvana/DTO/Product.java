package com.example.bagvana.DTO;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String productID;
    private String name;
    private String image;
    private String color;
    private String description;
    private int amount;
    private int price;

    private ArrayList<Comment> listComment;

    public Product() {
        this.listComment = null;
        this.productID = "";
        this.name = "";
        this.image = "";
        this.color = "";
        this.description = "";
        this.amount = 0;
        this.price = 0;
    }
    public Product(String productID, String name, String image, String color, String description, int amount, int price) {
        this.productID = productID;
        this.name = name;
        this.image = image;
        this.color = color;
        this.description = description;
        this.amount = amount;
        this.price = price;
    }
    public Product(String productID, String name, String image, String color, String description, int amount, int price, ArrayList<Comment> listComment) {
        this.productID = productID;
        this.name = name;
        this.image = image;
        this.color = color;
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.listComment = listComment;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Comment> getComments() { return listComment; }

    public boolean hasNameSimilarTo(String text) {
        return this.name.toLowerCase().contains(text.toLowerCase());
    }
}