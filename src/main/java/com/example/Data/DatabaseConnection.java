package com.example.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DatabaseConnection {

    private Connection db;
    private String url = "jdbc:sqlite:hospital.db";
    
    public DatabaseConnection() {
        // Constructor doesn't automatically open the connection
    }
    
    public Connection getConnection() {
        try {
            if (db == null || db.isClosed()) {
                db = DriverManager.getConnection(url);
            }
            return db;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public void closeConnection() {
        try {
            if (db != null && !db.isClosed()) {
                db.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
