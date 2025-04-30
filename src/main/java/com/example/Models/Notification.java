package com.example.Models;

public class Notification {
    
    public int id, user_id;
    public String message;
    public boolean read;

    public Notification(int id, String message, int user_id, boolean read) {
        this.id = id;
        this.message = message;
        this.user_id = user_id;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
