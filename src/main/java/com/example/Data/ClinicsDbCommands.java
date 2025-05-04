package com.example.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.example.Models.Clinic;
import com.example.Models.Person;

public class ClinicsDbCommands {

    private DatabaseConnection dbConnection = new DatabaseConnection();


    public List<Clinic> getAllClinics() {
        String query = "SELECT * FROM clinics ORDER BY id ASC";
        List<Clinic> clinics = new ArrayList<>();
        Connection connection = null;
        
        try {
            connection = dbConnection.getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                while (rs.next()) {
                    clinics.add(new Clinic(
                        rs.getInt("id"),
                        rs.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching clinics: " + e.getMessage());
        } finally {
            dbConnection.closeConnection();
        }
        
        return clinics;
    }
    
    public Clinic getClinic(int clinicId) {
        String query = "SELECT * FROM clinics WHERE id = ?";
        Connection connection = null;
        
        try {
            connection = dbConnection.getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, clinicId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Clinic(
                            rs.getInt("id"),
                            rs.getString("name")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching clinic: " + e.getMessage());
        } finally {
            dbConnection.closeConnection();
        }
        
        return null;
    }
    
    public List<Person> getDoctorsByClinic(int clinicId) {
        String query = "SELECT u.* FROM users u " +
                      "JOIN clinic_doctors cd ON u.id = cd.user_id " +
                      "WHERE cd.clinic_id = ? AND u.role = 'doctor' ORDER BY u.name";
        
        List<Person> doctors = new ArrayList<>();
        Connection connection = null;
        
        try {
            connection = dbConnection.getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
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
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctors: " + e.getMessage());
        } finally {
            dbConnection.closeConnection();
        }
        
        return doctors;
    }

    public boolean DeleteClinic(int id) {
        String query = "DELETE FROM clinics WHERE Id = " + id + ";";
        Connection connection = null;

        try {
            connection = dbConnection.getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            dbConnection.closeConnection();
        }
    }
}