package com.example.Models;

public class Appointment {
    private int id, patientId, doctorId, clinicId;
    private String day, time;

    public Appointment(int id, int patientId, int doctorId, int clinicId, String day, String time) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.day = day;
        this.time = time;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public int getClinicId() { return clinicId; }
    public String getDay() { return day; }
    public String getTime() { return time; }
}