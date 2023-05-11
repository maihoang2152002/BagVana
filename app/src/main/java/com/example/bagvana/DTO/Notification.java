package com.example.bagvana.DTO;

import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String id;
    private String title;
    private String message;
    private String image;
    private String time;
    private String status;

    public Notification() {};

    public Notification(String id, String title, String message, String image, String time, String status) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.image = image;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("message", message);
        result.put("image", image);
        result.put("time", time);
        result.put("status", status);

        return result;
    }
}
