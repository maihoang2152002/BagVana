package com.example.bagvana.DTO;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private String orderID, address, orderDate, status, userID;
    private Integer totalPrice;
    private ArrayList<Product> itemsOrdered;

    public Order() {
        orderID = "";
        address = "";
        orderDate = "";
        userID = "";
        status = "";
        totalPrice = 0;
        itemsOrdered = new ArrayList<>();
    }

    public Order(String orderID, ArrayList<Product> itemsOrdered,
                  String userID, String address, String orderDate,
                  Integer totalPrice, String status) {
        this.orderID = orderID;
        this.itemsOrdered = itemsOrdered;
        this.userID = userID;
        this.address = address;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getOrderID() { return orderID; }
    public ArrayList<Product> getItemsOrdered() { return itemsOrdered; }
    public String getUserID() { return userID; }
    public String getAddress() { return address; }
    public String getOrderDate() { return orderDate; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public void setItemsOrdered(ArrayList<Product> list) { itemsOrdered = list; };
}
