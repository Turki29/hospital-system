package com.example.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.example.Models.PatientRecord;

public class PatientsRecordsDbCommands extends DbConnection {
    

    public PatientRecord getRecordByUserId(int givenId)
    {
        String query = "SELECT * FROM records WHERE user_id = " + givenId + ";";
         PatientRecord resultRecord = null;

        try (
                Statement stmt = db.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int weight = rs.getInt("weight");
                int height = rs.getInt("height");
                String bloodPressure = rs.getString("blood_pressure");
                int sugar = rs.getInt("blood_sugar");
                String longtermDisease = rs.getString("long_term_diseases");

                resultRecord = new PatientRecord(id, userId, weight, height, bloodPressure, sugar, longtermDisease);
                return resultRecord;
            } 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching users", "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return new PatientRecord(-1, -1, -1, -1, "", -1, "");
    }

    public boolean insertPatientRecord(PatientRecord patientRecord)
    {
        String query = "INSERT INTO records (user_id, weight, height, blood_pressure, blood_sugar, long_term_diseases) VALUES (" +
                      patientRecord.getUserId() + ", " +
                      patientRecord.getWeight() + ", " +
                      patientRecord.getHeight() + ", '" +
                      patientRecord.getBloodPressure() + "', " +
                      patientRecord.getSugar() + ", '" +
                      patientRecord.getLongtermDisease() + "');";
        
        try (Statement stmt = db.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error inserting patient record", "Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
}
