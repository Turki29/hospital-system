package com.example;

import java.awt.FlowLayout;
import java.awt.GridLayout;

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

import com.example.Data.UsersDbCommands;
import com.example.Models.Person;

public class RegisterPage extends JFrame {

    private JLabel lblEmail, lblUsername, lblPassword, lblGender, lblAge, lblPhone;
    private JTextField txtEmail, txtUsername, txtPhone;
    private JPasswordField txtPassword;
    private JRadioButton male, female;
    private JComboBox<String> ageCombo;
    private JButton btnCreate, btnCancel;
    private UsersDbCommands userDb;

    public RegisterPage(String title) {
        super(title);

        setTitle("Create Account");
        setSize(350, 250);
        setLocation(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userDb = new UsersDbCommands();
        
        JPanel p1 = (JPanel) getContentPane();
        p1.setLayout(new GridLayout(7, 2, 5, 5));

        lblEmail = new JLabel("  Email:");
        txtEmail = new JTextField();

        lblUsername = new JLabel("  Username:");
        txtUsername = new JTextField();
        
        lblPhone = new JLabel("  Phone:");
        txtPhone = new JTextField();

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
        p1.add(lblPhone);
        p1.add(txtPhone);
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
    

    // Create the user account and insert into the database
    private void createAccount() {
        String email = txtEmail.getText();
        String phone  = txtPhone.getText();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "";
        String age = ageCombo.getSelectedItem().toString();
        
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Phone validation: either 10 digits starting with 05 or 12 digits starting with 9665
        if (!(phone.matches("^05\\d{8}$") || phone.matches("^9665\\d{8}$"))) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits starting with 05 or 12 digits starting with 9665.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Convert phone to 9665 format if it starts with 05
        if (phone.startsWith("05")) {
            phone = "966" + phone.substring(1);
        }



        // Email validation (contains @ and .)
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Password validation (at least 8 chars, one uppercase, one lowercase, one number)
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || 
            !password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, and one number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Username validation (at least 3 chars)
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Age validation (above 15)
        int selectedAge = Integer.parseInt(age);
        if (selectedAge <= 15) {
            JOptionPane.showMessageDialog(this, "You must be older than 15 to register.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       if(userDb.InsertUser(new Person(-1, username, email, phone, password, "patient", -1)))
       {
        JOptionPane.showMessageDialog(this, "Patient user added successfully, please login in log in window.", "Success", JOptionPane.INFORMATION_MESSAGE);
       }
       else {
        JOptionPane.showMessageDialog(this, "Failed to add patient user.", "Exception", JOptionPane.ERROR_MESSAGE);
       }
       
       
    }

    public static void main(String[] args) {
        new RegisterPage("Register Page");
    }
}
