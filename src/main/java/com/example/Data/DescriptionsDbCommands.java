package com.example.Data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.Models.Description;

public class DescriptionsDbCommands extends DbConnection {
 
    public int insertDescription(Description description) {
        String sql = "INSERT INTO descriptions (description,patient_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, description.getDescription());
            pstmt.setInt(2, description.getPatient_id());       
            
            pstmt.executeUpdate();
            
            // Get the generated ID
            var rs = pstmt.getGeneratedKeys();
            int descriptionId = -1;
            if (rs.next()) {
                descriptionId = rs.getInt(1);
                
                // Send notification to the patient
                String notificationSql = "INSERT INTO notifications (message, user_id) VALUES (?, ?)";
                try (PreparedStatement notificationStmt = db.prepareStatement(notificationSql)) {
                    notificationStmt.setString(1, "A new description has been added to your record");
                    notificationStmt.setInt(2, description.getPatient_id());
                    notificationStmt.executeUpdate();
                }
            }
            
            return descriptionId;
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return -1;
        }
    }
    
}
