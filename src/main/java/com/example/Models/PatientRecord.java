package com.example.Models;

public class PatientRecord {
    private int id;
    private int userId;
    private int weight;
    private int height;
    private String bloodPressure;
    private int sugar;
    private String longtermDisease;
    
    public PatientRecord() {
    }
    
    public PatientRecord(int id, int userId, int weight, int height, String bloodPressure, int sugar, String longtermDisease) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.height = height;
        this.bloodPressure = bloodPressure;
        this.sugar = sugar;
        this.longtermDisease = longtermDisease;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public String getBloodPressure() {
        return bloodPressure;
    }
    
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    
    public int getSugar() {
        return sugar;
    }
    
    public void setSugar(int sugar) {
        this.sugar = sugar;
    }
    
    public String getLongtermDisease() {
        return longtermDisease;
    }
    
    public void setLongtermDisease(String longtermDisease) {
        this.longtermDisease = longtermDisease;
    }
}
