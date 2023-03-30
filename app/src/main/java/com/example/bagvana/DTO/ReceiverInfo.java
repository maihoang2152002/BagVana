package com.example.bagvana.DTO;

public class ReceiverInfo {
    private String userID;
    private String addressID;
    private String address;
    private String fullName;
    private int phoneNumber;
    private boolean defaultAddress;

    public ReceiverInfo() {}

    public ReceiverInfo(String userID, String addressID, String address, String fullName, int phoneNumber, boolean isDefault) {
        this.userID = userID;
        this.addressID = addressID;
        this.address = address;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
