package com.example.Models;

public class Appointment {
    private int id, patientId, doctorId, clinicId;
    private String day, time;
    private String doctorName, patientName, clinicName;
    
    

    public Appointment(int id, int patientId, int doctorId, int clinicId, String day, String time) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.day = day;
        this.time = time;
    }


    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public int getClinicId() { return clinicId; }
    public String getDay() { return day; }
    public String getTime() { return time; }
}