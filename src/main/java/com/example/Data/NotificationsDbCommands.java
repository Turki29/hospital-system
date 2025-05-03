package com.example.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Models.Notification;

public class NotificationsDbCommands {
    
    private DatabaseConnection dbConnection = new DatabaseConnection();
    
    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        Connection connection = null;
        
        try {
            connection = dbConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM notifications WHERE user_id = ?");
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String message = rs.getString("message");
                int user_id = rs.getInt("user_id");
                boolean isRead = rs.getBoolean("read");
                
                notifications.add(new Notification(id, message, user_id, isRead));
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        
        return notifications;
    }

    public boolean markUserNotificationsAsRead(int userId) {
        Connection connection = null;
        boolean success = false;
        
        try {
            connection = dbConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE notifications SET read = true WHERE user_id = ?");
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        
        return success;
    }
}
