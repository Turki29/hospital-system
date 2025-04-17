// 
package com.example.AdminInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.Models.Person;


public class AdminInterface extends JFrame {

    JButton btnShowManageDoctors, btnShowManagePatients, btnShowManageSchedule;
    JPanel mainPanel;
    boolean disableMainWindow = false;
    boolean disableDoctorWindow = false;

    JLabel lbAdminName;
    Person admin;
   

    public AdminInterface(String title,Person admin) {
        super(title);

        this.admin = admin;
        lbAdminName = new JLabel("Admin Name: " + admin.getName() , JLabel.LEFT);

        JPanel panel = (JPanel) this.getContentPane();

        btnShowManageDoctors = new JButton("Manage Doctors");
        btnShowManageDoctors.addActionListener(e-> new AdminShowDoctors());

       btnShowManagePatients = new JButton("Manage Patients");
       btnShowManagePatients.addActionListener(e -> new AdminShowPatients()); 


       btnShowManageSchedule = new JButton("Manage Schedules");
       btnShowManageSchedule.addActionListener(e -> new AdminShowSchedules()); 
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        btnShowManageDoctors.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShowManagePatients.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShowManageSchedule.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(btnShowManageDoctors);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(btnShowManagePatients);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(btnShowManageSchedule);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonsPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}


