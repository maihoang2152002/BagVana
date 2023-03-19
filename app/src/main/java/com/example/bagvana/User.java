package com.example.bagvana;

public class User {
    private String id, name, phone;

    public User(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;

    }
    void setId(String ID){id = ID;};
    String getId() {return id;};
    void setName(String name){name = name;};
    String getName() {return name;};
    void setPhone(String phone){phone = phone;};
    String getPhone() {return phone;};
}
