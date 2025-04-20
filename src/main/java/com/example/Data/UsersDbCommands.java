package com.example.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.example.Models.Appointment;
import com.example.Models.Person;

public class UsersDbCommands extends DbConnection {

    public UsersDbCommands() {
        // this.InsertUser(new Person(-1, "trtsy", "email@email.com", "966507274092", "Asdqwe12", "admin", -1));
    }

    public Person getUser(int givenId) {
        String query = "SELECT * FROM users where id = " + givenId + ";";
        Person resultPerson = null;

        try (
                Statement stmt = db.createStatement();
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
                return resultPerson;
            } else {
                JOptionPane.showMessageDialog(null,
                        "User with id: '" + givenId + "' not found",
                        "NotFound",
                        JOptionPane.ERROR_MESSAGE);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching users", "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return new Person(-1, "", "", "", "", "", -1);
    }

    public int getUserIdByName(String name) {
        String query = "SELECT id FROM users WHERE name = '" + name + "';";
        
        try (
                Statement stmt = db.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                JOptionPane.showMessageDialog(null,
                        "User with name: '" + name + "' not found",
                        "NotFound",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching user ID", "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Return -1 to indicate user not found
    }

    public List<Person> getUsers(String condition) {
        String query;
        if (condition.isEmpty())
            query = "SELECT * FROM users;";
        else
            query = "SELECT * FROM users where " + condition + ";";

        List<Person> resultPersons = new ArrayList<>();

        try (
                Statement stmt = db.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int clinicId = rs.getInt("clinic_id");

                Person person = new Person( id,  name,  email,  phone,
                 password,  role,  clinicId);
                resultPersons.add(person);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching users", "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return resultPersons;

    }

    public boolean DeleteUser(int id) {
        String query = "DELETE FROM users WHERE Id = " + id + ";";

        try {
            Statement stmt = db.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);

        }
        return false;
    }

    public boolean InsertUser(Person person) {
        // Check for duplicate name, phone, or email
        String checkQuery = "SELECT COUNT(*) FROM users WHERE LOWER(name) = LOWER('" + person.getName() + 
                "') OR phone = '" + person.getPhoneNumber() + 
                "' OR LOWER(email) = LOWER('" + person.getEmail() + "')";

        try {
            Statement checkStmt = db.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkQuery);
            
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

            Statement stmt = db.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean UpdateUser(Person person)
    {
        String query = "UPDATE users SET name = '" + person.getName() +
                "', password = '" + person.getPassword() + 
                "', role = '" + person.getRole() + 
                "', phone = '" + person.getPhoneNumber() + 
                "', email = '" + person.getEmail() + 
                "', clinic_id = " + person.getClinicId() + 
                " WHERE id = " + person.getId() + ";";

        try {
            Statement stmt = db.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);

        }
        return false; 
    }

    public List<Person> getDoctorsPatients(int doctorId)
    {
        String query = "SELECT DISTINCT u.* FROM users u " +
                   "JOIN appointments a ON u.id = a.patient_id " +
                   "WHERE a.doctor_id = " + doctorId + " AND u.role = 'patient';";

        List<Person> resultPersons = new ArrayList<>();

        try (
            Statement stmt = db.createStatement();
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

        try {
            Statement stmt = db.createStatement();
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

        // Check if the doctor has another appointment at the selected day name and time
        String checkQuery = "SELECT COUNT(*) FROM appointments WHERE doctor_id = " +
                "(SELECT doctor_id FROM appointments WHERE id = " + apptId + ") " +
                "AND day = '" + newDayName + "' AND time = '" + newTime + "';";

        try {
            Statement checkStmt = db.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkQuery);

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, 
                        "The doctor already has an appointment at the selected day and time", 
                        "Conflict", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the appointment with the new day name and time
            String updateQuery = "UPDATE appointments SET day = '" + newDayName + 
                    "', time = '" + newTime + "' WHERE id = " + apptId + ";";

            Statement updateStmt = db.createStatement();
            int rowsAffected = updateStmt.executeUpdate(updateQuery);

            if (rowsAffected > 0) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error rescheduling appointment: " + e.getMessage(), 
                    "Exception", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public List<Appointment> getDoctorsAppointments(int doctorId) {
        String query = "SELECT a.id, a.patient_id, a.clinic_id, a.day, a.time, u.name AS patient_name " +
                       "FROM appointments a " +
                       "JOIN users u ON a.patient_id = u.id " +
                       "WHERE a.doctor_id = " + doctorId + ";";

        List<Appointment> resultAppointments = new ArrayList<>();

        try (
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                int clinicId = rs.getInt("clinic_id");
                String day = rs.getString("day");
                String time = rs.getString("time"); // Ensure time is fetched from the result set
                String patientName = rs.getString("patient_name");
                Appointment appointment = new Appointment(id, patientId, doctorId, 
                        clinicId, day, time);
                appointment.setPatientName(patientName); // Use the patientName variable
                resultAppointments.add(appointment);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctor's appointments", "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return resultAppointments;
    }
}

