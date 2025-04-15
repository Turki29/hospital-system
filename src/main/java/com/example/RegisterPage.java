package com.example;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class RegisterPage extends JFrame {

    private JLabel lblEmail, lblUsername, lblPassword, lblGender, lblAge;
    private JTextField txtEmail, txtUsername;
    private JPasswordField txtPassword;
    private JRadioButton male, female;
    private JComboBox<String> ageCombo;
    private JButton btnCreate, btnCancel;
    private Connection connection;

    public RegisterPage(String title) {
        super(title);

        setTitle("Create Account");
        setSize(350, 250);
        setLocation(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize SQLite connection
        connect();

        JPanel p1 = (JPanel) getContentPane();
        p1.setLayout(new GridLayout(6, 2, 5, 5));

        lblEmail = new JLabel("  Email:");
        txtEmail = new JTextField();

        lblUsername = new JLabel("  Username:");
        txtUsername = new JTextField();

        lblPassword = new JLabel("  Password:");
        txtPassword = new JPasswordField();

        lblGender = new JLabel("  Gender:");
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderPanel.add(male);
        genderPanel.add(female);

        lblAge = new JLabel("  Age:");
        String[] ages = new String[130];
        for (int i = 0; i < 130; i++) {
            ages[i] = String.valueOf(i + 1); // Convert numbers to String
        }
        ageCombo = new JComboBox<>(ages);

        btnCancel = new JButton("Cancel");
        btnCreate = new JButton("Create");

        p1.add(lblEmail);
        p1.add(txtEmail);
        p1.add(lblUsername);
        p1.add(txtUsername);
        p1.add(lblPassword);
        p1.add(txtPassword);
        p1.add(lblGender);
        p1.add(genderPanel);
        p1.add(lblAge);
        p1.add(ageCombo);
        p1.add(btnCreate);
        p1.add(btnCancel);

        // Button actions
        btnCreate.addActionListener(e -> createAccount());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Connect to SQLite database
    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:hospital.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create the user account and insert into the database
    private void createAccount() {
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "";
        String age = ageCombo.getSelectedItem().toString();
        
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            String insertUserSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(insertUserSQL);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "patient"); // Default role is "patient" (can be changed later)
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Insert user info into appropriate table (e.g., patients, doctors, admins)
                String insertPatientSQL = "INSERT INTO patients (user_id, firstname, lastname, phoneNumber, email) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?, ?, ?)";
                stmt = connection.prepareStatement(insertPatientSQL);
                stmt.setString(1, username);
                stmt.setString(2, "FirstName");  // Replace with real input fields
                stmt.setString(3, "LastName");   // Replace with real input fields
                stmt.setString(4, "PhoneNumber"); // Replace with real input fields
                stmt.setString(5, email);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account created successfully!");
                dispose(); // Close registration form
            } else {
                JOptionPane.showMessageDialog(this, "Error creating account. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterPage("Register Page");
    }
}
