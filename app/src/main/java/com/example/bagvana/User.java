package com.example.bagvana;

public class User {
    private String id,phone, username, password;

    public User() {
    }

    public User(String id, String phone, String username, String password) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

