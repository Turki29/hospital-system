package com.example.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Models.Notification;



public class NotificationsDbCommands extends DbConnection {
    

    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();

        try(PreparedStatement stmt = db.prepareStatement("SELECT * FROM notifications WHERE user_id = ?")) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String message = rs.getString("message");
                int user_id = rs.getInt("user_id");
                boolean isRead = rs.getBoolean("read");
                
                notifications.add(new Notification(id, message, user_id,isRead ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notifications;
    }


    public boolean markUserNotificationsAsRead(int userId) {
        try(PreparedStatement stmt = db.prepareStatement("UPDATE notifications SET read = true WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
