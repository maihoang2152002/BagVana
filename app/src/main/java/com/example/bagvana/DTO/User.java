package com.example.bagvana.DTO;

public class User {
    private String id, phone, username, password, dob, gender, typeUser, email, avatar, fullname;


    public User() {
        this.id = "";
        this.phone = "";
        this.username = "";
        this.password = "";
        this.dob = "";
        this.gender = "";
        this.typeUser = "1";
        this.email = "";
        this.avatar = "";
        this.fullname = "";
    }

    public User(String id, String phone, String username, String password, String dob, String gender, String typeUser, String email, String avatar, String fullname) {
        this.id = id;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.typeUser = typeUser;
        this.email = email;
        this.avatar = avatar;
        this.fullname = fullname;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String toString(){
        return avatar + '\n' + dob + '\n' + email + '\n' + fullname + '\n' + gender + '\n' + id + '\n' + password + '\n' + phone + '\n' + typeUser + '\n' + username + '\n';
    }

    public void ResetUser() {
        this.id = "";
        this.phone = "";
        this.username = "";
        this.password = "";
        this.dob = "";
        this.gender = "";
        this.typeUser = "";
        this.email = "";
        this.avatar = "";
        this.fullname = "";
    }
}

