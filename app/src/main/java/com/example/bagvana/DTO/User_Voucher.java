package com.example.bagvana.DTO;

public class User_Voucher {
    private String voucherID;
    private String userID;
    private int amount;

    public User_Voucher() {}

    public User_Voucher(String voucherID, String userID, int amount) {
        this.voucherID = voucherID;
        this.userID = userID;
        this.amount = amount;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
