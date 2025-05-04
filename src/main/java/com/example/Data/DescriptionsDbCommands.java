package com.example.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.Models.Description;

public class DescriptionsDbCommands {
 
    private DatabaseConnection dbConnection = new DatabaseConnection();

    public int insertDescription(Description description) {
        Connection connection = null;
        int descriptionId = -1;
        
        try {
            connection = dbConnection.getConnection();
            String sql = "INSERT INTO descriptions (description,patient_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, description.getDescription());
                pstmt.setInt(2, description.getPatient_id());       
                
                pstmt.executeUpdate();
                
                // Get the generated ID
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    descriptionId = rs.getInt(1);
                    
                    // Send notification to the patient
                    String notificationSql = "INSERT INTO notifications (message, user_id) VALUES (?, ?)";
                    try (PreparedStatement notificationStmt = connection.prepareStatement(notificationSql)) {
                        notificationStmt.setString(1, "A new prescribtion with id "+ description.id +" has been added to your record");
                        notificationStmt.setInt(2, description.getPatient_id());
                        notificationStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            descriptionId = -1;
        } finally {
            if (connection != null) {
                dbConnection.closeConnection();
            }
        }
        
        return descriptionId;
    }
}
