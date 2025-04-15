package com.example.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DbConnection {

    protected Connection db;
    public DbConnection() {

        String url = "jdbc:sqlite:hospital.db"; // اسم قاعدة البيانات

        try{
            this.db = DriverManager.getConnection(url);
           
       
            

        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    

   
}
