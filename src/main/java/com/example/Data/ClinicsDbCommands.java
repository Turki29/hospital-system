package com.example.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.example.Models.Clinic;
import com.example.Models.Person;

public class ClinicsDbCommands extends DbConnection {

    public List<Clinic> getAllClinics() {
        String query = "SELECT * FROM clinics ORDER BY name";
        List<Clinic> clinics = new ArrayList<>();
        
        try (Statement stmt = db.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                clinics.add(new Clinic(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching clinics: " + e.getMessage());
        }
        
        return clinics;
    }
    
    public Clinic getClinic(int clinicId) {
        String query = "SELECT * FROM clinics WHERE id = ?";
        
        try (PreparedStatement pstmt = db.prepareStatement(query)) {
            pstmt.setInt(1, clinicId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Clinic(
                        rs.getInt("id"),
                        rs.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching clinic: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Person> getDoctorsByClinic(int clinicId) {
        String query = "SELECT u.* FROM users u " +
                      "JOIN clinic_doctors cd ON u.id = cd.user_id " +
                      "WHERE cd.clinic_id = ? AND u.role = 'doctor' ORDER BY u.name";
        
        List<Person> doctors = new ArrayList<>();
        
        try (PreparedStatement pstmt = db.prepareStatement(query)) {
            pstmt.setInt(1, clinicId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getInt("clinic_id")
                    ));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctors: " + e.getMessage());
        }
        
        return doctors;
    }

}