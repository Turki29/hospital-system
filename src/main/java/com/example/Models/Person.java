package com.example.Models;

import java.util.LinkedList;

public class Person {
    
    public int id, clinicId;
    public String name, phoneNumber, email, password, role;
    
   

    public Person(int id, String name, String email, String phoneNumber,
     String password, String role, int clinicId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.clinicId = clinicId;
        
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getClinicId() {
        return clinicId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }
    
  
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setAppointments(LinkedList<Appointment> appointments) {
        // this.appointments = appointments;
    }
}
