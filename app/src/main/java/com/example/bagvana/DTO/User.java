package com.example.bagvana.DTO;

public class User {
    private String id,phone, username, password,BoD,gender,typeUser = "1",email, avatar;

    public User() {
    }

    public User(String id, String phone, String username, String password, String boD, String gender, String typeUser, String email, String avatar) {
        this.id = id;
        this.phone = phone;
        this.username = username;
        this.password = password;
        BoD = boD;
        this.gender = gender;
        this.typeUser = typeUser;
        this.email = email;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBoD() {
        return BoD;
    }

    public void setBoD(String boD) {
        BoD = boD;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

