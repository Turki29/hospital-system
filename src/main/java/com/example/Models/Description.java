package com.example.Models;

public class Description {
    

    public int id, patient_id;
    public String description;

    public Description(int id, String description, int patient_id) {
        
        this.id = id;
        this.description = description;
        this.patient_id = patient_id;
    }

    public int getId() {
        return id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
