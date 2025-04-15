package com.example.Models;

public class Clinic {
    private int id;
    private String name;

    public Clinic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return name;
    }
}