package com.example.Models;

public class Doctor extends Person {
    public String speciality, startWorkingHours, endWorkingHours;

    public Doctor(int id, String name, String email, String phoneNumber, String password, 
                  String role, int clinicId, String speciality, String startWorkingHours, 
                  String endWorkingHours) {
        super(id, name, email, phoneNumber, password, role, clinicId);
        this.speciality = speciality;
        this.startWorkingHours = startWorkingHours;
        this.endWorkingHours = endWorkingHours;
    }
}
