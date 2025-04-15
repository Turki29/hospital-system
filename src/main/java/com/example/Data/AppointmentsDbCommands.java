package com.example.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.Models.Appointment;

public class AppointmentsDbCommands extends DbConnection {

    public boolean insertAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, clinic_id, day, time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());       
            pstmt.setInt(3, a.getClinicId());   
            pstmt.setString(4, a.getDay());
            pstmt.setString(5, a.getTime());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

   
    
    public List<Appointment> getAppointmentsForPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.id, a.patient_id, a.doctor_id, a.clinic_id, a.day, a.time " +
                     "FROM appointments a " +
                     "WHERE a.patient_id = ?";
        
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("clinic_id"),
                    rs.getString("day"),
                    rs.getString("time")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching patient appointments: " + e.getMessage());
        }
        
        return appointments;
    }

    public boolean isAppointmentSlotTaken(int doctorId, String day, String time) {
        String query = "SELECT COUNT(*) AS count FROM appointments " +
                       "WHERE doctor_id = ? AND day = ? AND time = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            stmt.setString(2, day);
            stmt.setString(3, time);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("count") > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error checking appointment slot: " + e.getMessage());
            return true; // Return as taken if there's an error
        }
    }

    public List<String> getClinics() {
        List<String> clinics = new ArrayList<>();
        String sql = "SELECT name FROM clinics";
        try (Statement stmt = db.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clinics.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching clinics: " + e.getMessage());
        }
        return clinics;
    }

    public List<String> getDoctorsByClinic(int clinicId) {
        List<String> doctors = new ArrayList<>();
        String sql = "SELECT u.name FROM users u " +
                     "WHERE u.clinic_id = ? AND u.role = 'doctor'";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, clinicId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching doctors by clinic: " + e.getMessage());
        }
        return doctors;
    }

    public int getClinicIdByName(String clinicName) {
        String sql = "SELECT id FROM clinics WHERE name = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, clinicName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching clinic ID: " + e.getMessage());
        }
        return -1;
    }

    public int getDoctorIdByName(String doctorName) {
        String sql = "SELECT id FROM users WHERE name = ? AND role = 'doctor'";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching doctor ID: " + e.getMessage());
        }
        return -1;
    }

    public List<String> searchDoctorsByName(String keyword) {
        List<String> doctors = new ArrayList<>();
        String sql = "SELECT name FROM users WHERE role = 'doctor' AND name LIKE ?";
        
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching doctors: " + e.getMessage());
        }
        
        return doctors;
    }
}
