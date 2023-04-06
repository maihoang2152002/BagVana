package com.example.bagvana.DTO;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {
    private String orderID;
    private String userID;
    private String orderDate;
    private ReceiverInfo receiverInfo;
    private int totalPrice;
    private String status;
    private String paymentMethod;
    private HashMap<String, Integer> usedVoucher;

    private HashMap<String, Product> itemsOrdered;


    public HashMap<String, Integer> getUsedVoucher() {
        return usedVoucher;
    }

    public void setUsedVoucher(HashMap<String, Integer> usedVoucher) {
        this.usedVoucher = usedVoucher;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public ReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(ReceiverInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public HashMap<String, Product> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(HashMap<String, Product> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }
}
