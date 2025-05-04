package com.example.AdminInterface;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.example.Data.AppointmentsDbCommands;
import com.example.Data.ClinicsDbCommands;
import com.example.Data.UsersDbCommands;

public class AdminShowStatistics extends JFrame {
    

     UsersDbCommands userDb;
     AppointmentsDbCommands appDb;
     ClinicsDbCommands clinicDb;

    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JButton addButton, cancelButton;
    
    public AdminShowStatistics() {
        userDb = new UsersDbCommands();
        appDb = new AppointmentsDbCommands();
        clinicDb = new ClinicsDbCommands();

        setTitle("Statistics of the hospital");
        setSize(400, 300);
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
        formPanel.add(new JLabel("Statistics of the hospital:"), gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Patients number:"), gbc);
        
        gbc.gridx = 1;
        int patientNumber = userDb.getUsers("role = 'patient'").size();
        formPanel.add(new JLabel(patientNumber + ""), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Doctors number:"), gbc);
        
        gbc.gridx = 1;
        int doctorNumber = userDb.getUsers("role = 'doctor'").size();
        formPanel.add(new JLabel(doctorNumber + ""), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Appointments number:"), gbc);
        
        gbc.gridx = 1;
        int appointmentsNumber = appDb.getAppointments().size();
        formPanel.add(new JLabel(appointmentsNumber + ""), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Clinics number:"), gbc);
        
        gbc.gridx = 1;
        int clinicsNumber = clinicDb.getAllClinics().size();
        formPanel.add(new JLabel(clinicsNumber + ""), gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
       setVisible(true);
    }
        
      

}
