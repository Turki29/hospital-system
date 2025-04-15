package com.example.AdminInterface;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.example.Data.UsersDbCommands;
import com.example.Models.Person;

public class AdminShowAddDoctorWindow extends JFrame {
    
    UsersDbCommands userDb;

    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> specialtyComboBox, clinicComboBox;
    private JPasswordField passwordField;
    private JButton addButton, cancelButton;
    
    // Sample data for dropdown menus
    private String[] specialties = {"Cardiology", "Dermatology", "Neurology", "Orthopedics", "Pediatrics", "Psychiatry", "Ophthalmology"};
    private String[] clinics = {"Main Clinic", "North Branch", "South Branch", "East Wing", "West Wing"};
    
    public AdminShowAddDoctorWindow() {
        setTitle("Add New Doctor");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);
        
        // Form panel with GridBagLayout for better alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Doctor Name:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Doctor Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Specialty:"), gbc);
        
        gbc.gridx = 1;
        specialtyComboBox = new JComboBox<>(specialties);
        formPanel.add(specialtyComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Clinic Name:"), gbc);
        
        gbc.gridx = 1;
        clinicComboBox = new JComboBox<>(clinics);
        formPanel.add(clinicComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Doctor");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from fields
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String specialty = (String) specialtyComboBox.getSelectedItem();
                String clinic = (String) clinicComboBox.getSelectedItem();
                String password = new String(passwordField.getPassword());
                
                // Validate fields
                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
                   password.isEmpty() || specialty == null || clinic == null) {
                    JOptionPane.showMessageDialog(AdminShowAddDoctorWindow.this, 
                        "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                userDb = new UsersDbCommands();
                if(userDb.InsertUser(new Person(-1, name, email, phone, password, "doctor", clinicComboBox.getSelectedIndex())))
                {
                    JOptionPane.showMessageDialog(AdminShowAddDoctorWindow.this, 
                    "Doctor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                }
                else {
                    JOptionPane.showMessageDialog(AdminShowAddDoctorWindow.this, 
                        "Failed to add doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        setVisible(true);
    }
    
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        specialtyComboBox.setSelectedIndex(0);
        clinicComboBox.setSelectedIndex(0);
        passwordField.setText("");
    }
}
