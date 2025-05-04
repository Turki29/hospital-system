package com.example.PatientInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.example.Data.NotificationsDbCommands;
import com.example.Models.Notification;
import com.example.Models.Person;

public class PatientsInterface extends JFrame {
    private JLabel lbDashboard, lbPatientName;
    private JButton btnBookAppointment, btnViewAppointments, btnExit, btnNotifications;
    private Person patient;
    private NotificationsDbCommands notfiyDb;

    public PatientsInterface(String title, Person patient) {
        super(title);
        this.patient = patient;

        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(100, 149, 237));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lbPatientName = new JLabel("Welcome, " + patient.getName(), JLabel.LEFT);
        lbPatientName.setFont(new Font("Arial", Font.BOLD, 16));
        lbPatientName.setForeground(Color.WHITE);
        headerPanel.add(lbPatientName, BorderLayout.WEST);

        // Dashboard title
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        lbDashboard = new JLabel("Patient Dashboard", SwingConstants.CENTER);
        lbDashboard.setFont(new Font("Arial", Font.BOLD, 24));
        lbDashboard.setForeground(new Color(70, 130, 180));
        centerPanel.add(lbDashboard, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(240, 248, 255));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        // Create and style buttons
        btnBookAppointment = createStyledButton("Book Appointment");
        btnViewAppointments = createStyledButton("View Appointments");
        btnExit = createStyledButton("Exit");

        btnBookAppointment.addActionListener(new Book(patient));
        btnViewAppointments.addActionListener(new ViewAppointments(patient));
        btnExit.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        notfiyDb = new NotificationsDbCommands();
        List<Notification> notifications = notfiyDb.getUserNotifications(patient.id);

        int unreadNotifications = 0;
        for (Notification notification : notifications) {
            if (!notification.isRead())
                unreadNotifications++;
        }

        btnNotifications = createStyledButton(
            unreadNotifications == 0 ? "Notifications" : "Notifications (" + unreadNotifications + ")");
        
        if (unreadNotifications > 0) {
            btnNotifications.setBackground(new Color(255, 102, 102));
            btnNotifications.setForeground(Color.WHITE);
        }

        btnNotifications.addActionListener(e -> {
            List<Notification> currentNotifications = notfiyDb.getUserNotifications(patient.id);
            JDialog notificationDialog = new JDialog(this, "Notifications", true);
            notificationDialog.setLayout(new BorderLayout());
            notificationDialog.getContentPane().setBackground(Color.WHITE);

            JPanel notificationPanel = new JPanel();
            notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
            notificationPanel.setBackground(Color.WHITE);
            
            if (currentNotifications.isEmpty()) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));

                JLabel label = new JLabel("You have no notifications");
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                panel.add(label, BorderLayout.CENTER);

                panel.setBackground(Color.WHITE);
                notificationPanel.add(panel);
            }
            
            for (Notification notification : currentNotifications) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));

                JLabel label = new JLabel(notification.message);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                if (!notification.read) {
                    label.setForeground(new Color(220, 20, 60));
                    label.setFont(new Font("Arial", Font.BOLD, 14));
                }
                panel.add(label, BorderLayout.CENTER);

                panel.setBackground(Color.WHITE);
                notificationPanel.add(panel);
            }

            JScrollPane scrollPane = new JScrollPane(notificationPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            notificationDialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.setFont(new Font("Arial", Font.BOLD, 12));
            closeButton.setBackground(new Color(70, 130, 180));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            closeButton.setFocusPainted(false);
            
            closeButton.addActionListener(event -> {
                notfiyDb.markUserNotificationsAsRead(patient.id);
                btnNotifications.setText("Notifications");
                btnNotifications.setBackground(new Color(70, 130, 180));
                btnNotifications.setForeground(Color.WHITE);
                notificationDialog.dispose();
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(closeButton);
            notificationDialog.add(buttonPanel, BorderLayout.SOUTH);

            notificationDialog.setSize(450, 350);
            notificationDialog.setLocationRelativeTo(this);
            notificationDialog.setVisible(true);
        });

        // Add each button to panel with spacing
        buttonsPanel.add(btnBookAppointment);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnViewAppointments);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnNotifications);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnExit);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setVisible(true);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(CENTER_ALIGNMENT);
        return button;
    }

   
}