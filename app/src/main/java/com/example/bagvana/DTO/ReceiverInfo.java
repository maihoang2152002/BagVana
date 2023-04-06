package com.example.bagvana.DTO;

import java.io.Serializable;

public class ReceiverInfo implements Serializable {
    private String userID;
    private String addressID;
    private String address;
    private String fullName;
    private String phone;
    private boolean defaultAddress;

    public ReceiverInfo() {}

    public ReceiverInfo(String userID, String addressID, String address, String fullName, String phone, boolean isDefault) {
        this.userID = userID;
        this.addressID = addressID;
        this.address = address;
        this.fullName = fullName;
        this.phone = phone;
        this.defaultAddress = isDefault;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
