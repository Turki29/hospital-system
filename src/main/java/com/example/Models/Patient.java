package com.example.Models;

public class Patient extends Person {
    // Personal details (inherited from Person class)
    public int id;

    // Medical information
    public int weight;
    public int height;
    public int bloodPressure;
    public int bloodSugar;
    public String longTermDisease;

    // Constructor that initializes both personal and medical details
    public Patient(int id, String name, String email, String phoneNumber
    , String password, String role, int clinicId,
    int weight, int height, int bloodPressure,int bloodSugar
    , String longTermDisease) {
        super(id, name, email, phoneNumber, password, role, clinicId); // Call to the Person constructor
        this.weight = weight;
        this.height = height;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
        this.longTermDisease = longTermDisease;
    }

   
}