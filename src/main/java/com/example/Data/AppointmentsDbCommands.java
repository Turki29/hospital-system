package com.example.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.Models.Appointment;
import com.example.Models.Person;

public class AppointmentsDbCommands {
    
    private DatabaseConnection dbConnection = new DatabaseConnection();

    public boolean insertAppointment(Appointment a) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "INSERT INTO appointments (patient_id, doctor_id, clinic_id, day, time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setInt(1, a.getPatientId());
                pstmt.setInt(2, a.getDoctorId());       
                pstmt.setInt(3, a.getClinicId());   
                pstmt.setString(4, a.getDay());
                pstmt.setString(5, a.getTime());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name, c.name AS clinic_name " + 
                    "FROM appointments a " + 
                    "JOIN users p ON a.patient_id = p.id " +
                    "JOIN users d ON a.doctor_id = d.id " + 
                    "JOIN clinics c ON a.clinic_id = c.id ";
                
            try (PreparedStatement stmt = db.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("clinic_id"),
                        rs.getString("day"),
                        rs.getString("time")
                    );
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointment.setClinicName(rs.getString("clinic_name"));
                    appointment.setPatientName(rs.getString("patient_name"));
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Error fetching appointments: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return appointments;
    }
    
    public List<Appointment> getAppointmentsForPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        Connection db = null;
        try {
            db = dbConnection.getConnection();
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
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching patient appointments: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return appointments;
    }

    public boolean isAppointmentSlotTaken(int doctorId, String day, String time) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String query = "SELECT COUNT(*) AS count FROM appointments " +
                          "WHERE doctor_id = ? AND day = ? AND time = ?";
            try (PreparedStatement stmt = db.prepareStatement(query)) {
                stmt.setInt(1, doctorId);
                stmt.setString(2, day);
                stmt.setString(3, time);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking appointment slot: " + e.getMessage());
            return true; // Return as taken if there's an error
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<String> getClinics() {
        List<String> clinics = new ArrayList<>();
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT name FROM clinics ORDER BY id ASC";
            try (Statement stmt = db.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    clinics.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching clinics: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return clinics;
    }

    public List<Person> getDoctorsByClinic(int clinicId) {
        List<Person> doctors = new ArrayList<>();
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT * FROM users WHERE clinic_id = ?";
            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setInt(1, clinicId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Person doctor = new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("clinic_id")
                    );
                    doctors.add(doctor);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching doctors by clinic: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return doctors;
    }

    public int getClinicIdByName(String clinicName) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT id FROM clinics WHERE name = ?";
            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setString(1, clinicName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching clinic ID: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public int getDoctorIdByName(String doctorName) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT id FROM users WHERE name = ? AND role = 'doctor'";
            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setString(1, doctorName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching doctor ID: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public List<String> searchDoctorsByName(String keyword) {
        List<String> doctors = new ArrayList<>();
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "SELECT name FROM users WHERE role = 'doctor' AND name LIKE ?";
            
            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setString(1, "%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    doctors.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching doctors: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
        return doctors;
    }

    public boolean UpdateAppointment(Appointment appointment) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, clinic_id = ?, day = ?, time = ? WHERE id = ?";
            
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setInt(1, appointment.getPatientId());
                pstmt.setInt(2, appointment.getDoctorId());
                pstmt.setInt(3, appointment.getClinicId());
                pstmt.setString(4, appointment.getDay());
                pstmt.setString(5, appointment.getTime());
                pstmt.setInt(6, appointment.getId());
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("⚠️ No appointments were updated. ID might not exist: " + appointment.getId());
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating appointment: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public boolean DeleteAppointment(int id) {
        Connection db = null;
        try {
            db = dbConnection.getConnection();
            String sql = "DELETE FROM appointments WHERE id = ?";

            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("⚠️ No appointments were deleted. ID might not exist: " + id);
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting appointment: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
