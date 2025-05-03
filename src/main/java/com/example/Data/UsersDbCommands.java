package com.example.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.example.Models.Appointment;
import com.example.Models.Person;

public class UsersDbCommands {
    
    private DatabaseConnection dbConnection = new DatabaseConnection();
    private Connection connection;

    public UsersDbCommands() {
        
    }

    public Person getUser(int givenId) {
        String query = "SELECT * FROM users where id = " + givenId + ";";
        Person resultPerson = null;
        connection = dbConnection.getConnection();

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int clinicId = rs.getInt("clinic_id");

                resultPerson = new Person(id, name, password, role, phone, email, clinicId);
            } else {
                JOptionPane.showMessageDialog(null,
                        "User with id: '" + givenId + "' not found",
                        "NotFound",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching users", "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return resultPerson != null ? resultPerson : new Person(-1, "", "", "", "", "", -1);
    }

    public int getUserIdByName(String name) {
        String query = "SELECT id FROM users WHERE name = '" + name + "';";
        int result = -1;
        connection = dbConnection.getConnection();
        
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                result = rs.getInt("id");
            } else {
                JOptionPane.showMessageDialog(null,
                        "User with name: '" + name + "' not found",
                        "NotFound",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching user ID", "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return result;
    }

    public List<Person> getUsers(String condition) {
        String query;
        if (condition.isEmpty())
            query = "SELECT * FROM users;";
        else
            query = "SELECT * FROM users where " + condition + ";";

        List<Person> resultPersons = new ArrayList<>();
        connection = dbConnection.getConnection();

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int clinicId = rs.getInt("clinic_id");

                Person person = new Person(id, name, email, phone,
                 password, role, clinicId);
                resultPersons.add(person);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching users", "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return resultPersons;
    }

    public boolean DeleteUser(int id) {
        String query = "DELETE FROM users WHERE Id = " + id + ";";
        boolean success = false;
        connection = dbConnection.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return success;
    }

    public boolean InsertUser(Person person) {
        // Validate input data
        if (person.getName().isEmpty() || person.getEmail().isEmpty() || 
            person.getPassword().isEmpty() || person.getPhoneNumber().isEmpty() || person.getRole().isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Username validation (at least 3 chars)
        if (person.getName().length() < 3) {
            JOptionPane.showMessageDialog(null, "Username must be at least 3 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Phone validation: either 10 digits starting with 05 or 12 digits starting with 9665
        String phone = person.getPhoneNumber();
        if (!(phone.matches("^05\\d{8}$") || phone.matches("^9665\\d{8}$"))) {
            JOptionPane.showMessageDialog(null, "Phone number must be 10 digits starting with 05 or 12 digits starting with 9665.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Convert phone to 9665 format if it starts with 05
        if (phone.startsWith("05")) {
            phone = "966" + phone.substring(1);
            person.setPhoneNumber(phone);
        }

        // Email validation (contains @ and .)
        if (!person.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Password validation (at least 8 chars, one uppercase, one lowercase, one number)
        String password = person.getPassword();
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || 
            !password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, and one number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        connection = dbConnection.getConnection();
        boolean success = false;

        try {
            // Check for duplicate name, phone, or email
            String checkQuery = "SELECT COUNT(*) FROM users WHERE LOWER(name) = LOWER('" + person.getName() + 
                    "') OR phone = '" + person.getPhoneNumber() + 
                    "' OR LOWER(email) = LOWER('" + person.getEmail() + "')";

            try (Statement checkStmt = connection.createStatement();
                ResultSet rs = checkStmt.executeQuery(checkQuery)) {
                
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, 
                            "User with the same name, phone, or email already exists", 
                            "Duplicate Entry", 
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                String query = "INSERT INTO users (name, password, role, phone, email, clinic_id)" +
                        "VALUES ('" +
                        person.getName() + "','" +
                        person.getPassword() + "','" +
                        person.getRole() + "','" +
                        person.getPhoneNumber() + "','" +
                        person.getEmail() + "'," +
                        person.getClinicId() + ")";

                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(query);
                    success = true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return success;
    }

    public boolean UpdateUser(Person person) {
        String query = "UPDATE users SET name = '" + person.getName() +
                "', password = '" + person.getPassword() + 
                "', role = '" + person.getRole() + 
                "', phone = '" + person.getPhoneNumber() + 
                "', email = '" + person.getEmail() + 
                "', clinic_id = " + person.getClinicId() + 
                " WHERE id = " + person.getId() + ";";

        boolean success = false;
        connection = dbConnection.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return success; 
    }

    public List<Person> getDoctorsPatients(int doctorId) {
        String query = "SELECT DISTINCT u.* FROM users u " +
                   "JOIN appointments a ON u.id = a.patient_id " +
                   "WHERE a.doctor_id = " + doctorId + " AND u.role = 'patient';";

        List<Person> resultPersons = new ArrayList<>();
        connection = dbConnection.getConnection();

        try (
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int clinicId = rs.getInt("clinic_id");

                Person person = new Person(id, name, email, phone,
                password, role, clinicId);
                resultPersons.add(person);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctor's patients", "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return resultPersons;
    }
 
    public List<Person> getDoctors() {
        return getUsers("role = 'doctor'");
    }
    
    public List<Person> searchDoctorsByName(String keyword) {
        return getUsers("role = 'doctor' AND name LIKE '%" + keyword + "%'");
    }

    public void cancelAppointment(int apptId) {
        String query = "DELETE FROM appointments WHERE id = " + apptId + ";";
        connection = dbConnection.getConnection();

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, 
                        "Appointment canceled successfully", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                        "No appointment found with ID: " + apptId, 
                        "Not Found", 
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error canceling appointment: " + e.getMessage(), 
                    "Exception", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
    }

    public void rescheduleAppointment(int apptId, String newDayName, String newTime) {
        if (newDayName == null || newDayName.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Enter the day you want to reschedule to (e.g., Monday, Tuesday)", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate the new time
        if (newTime == null || newTime.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Please enter a valid time (e.g., 10:00 AM)", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        connection = dbConnection.getConnection();

        try {
            // Check if the doctor has another appointment at the selected day name and time
            String checkQuery = "SELECT COUNT(*) FROM appointments WHERE doctor_id = " +
                    "(SELECT doctor_id FROM appointments WHERE id = " + apptId + ") " +
                    "AND day = '" + newDayName + "' AND time = '" + newTime + "';";

            try (Statement checkStmt = connection.createStatement();
                ResultSet rs = checkStmt.executeQuery(checkQuery)) {

                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, 
                            "The doctor already has an appointment at the selected day and time", 
                            "Conflict", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Get the original appointment details before updating
            String getOriginalQuery = "SELECT day, time, doctor_id, patient_id FROM appointments WHERE id = " + apptId + ";";
            try (Statement getStmt = connection.createStatement();
                ResultSet originalRs = getStmt.executeQuery(getOriginalQuery)) {
                
                if (originalRs.next()) {
                    String oldDay = originalRs.getString("day");
                    String oldTime = originalRs.getString("time");
                    int doctorId = originalRs.getInt("doctor_id");
                    int patientId = originalRs.getInt("patient_id");
                    
                    // Update the appointment with the new day name and time
                    String updateQuery = "UPDATE appointments SET day = '" + newDayName + 
                            "', time = '" + newTime + "' WHERE id = " + apptId + ";";

                    try (Statement updateStmt = connection.createStatement()) {
                        int rowsAffected = updateStmt.executeUpdate(updateQuery);

                        if (rowsAffected > 0) {
                            // Send notification to doctor
                            String doctorMsg = "Your appointment has been rescheduled from " + oldDay + " " + oldTime + 
                                            " to " + newDayName + " " + newTime;
                            String docNotifyQuery = "INSERT INTO notifications (message, user_id) VALUES ('" + 
                                                doctorMsg + "', " + doctorId + ");";
                            
                            // Send notification to patient
                            String patientMsg = "Your appointment has been rescheduled from " + oldDay + " " + oldTime + 
                                            " to " + newDayName + " " + newTime;
                            String patientNotifyQuery = "INSERT INTO notifications (message, user_id) VALUES ('" + 
                                                    patientMsg + "', " + patientId + ");";
                            
                            // Execute notification inserts
                            try (Statement notifyStmt = connection.createStatement()) {
                                notifyStmt.executeUpdate(docNotifyQuery);
                                notifyStmt.executeUpdate(patientNotifyQuery);
                            }
                            
                            JOptionPane.showMessageDialog(null, 
                                    "Appointment rescheduled successfully", 
                                    "Success", 
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                    "No appointment found with ID: " + apptId, 
                                    "Not Found", 
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, 
                            "No appointment found with ID: " + apptId, 
                            "Not Found", 
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error rescheduling appointment: " + e.getMessage(), 
                    "Exception", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
    }
    
    public List<Appointment> getDoctorsAppointments(int doctorId) {
        String query = "SELECT a.id, a.patient_id, a.clinic_id, a.day, a.time, u.name AS patient_name " +
                       "FROM appointments a " +
                       "JOIN users u ON a.patient_id = u.id " +
                       "WHERE a.doctor_id = " + doctorId + ";";

        List<Appointment> resultAppointments = new ArrayList<>();
        connection = dbConnection.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                int clinicId = rs.getInt("clinic_id");
                String day = rs.getString("day");
                String time = rs.getString("time");
                String patientName = rs.getString("patient_name");
                Appointment appointment = new Appointment(id, patientId, doctorId, 
                        clinicId, day, time);
                appointment.setPatientName(patientName);
                resultAppointments.add(appointment);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctor's appointments", "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbConnection.closeConnection();
        }
        return resultAppointments;
    }
}
