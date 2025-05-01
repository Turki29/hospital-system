package com.example.AdminInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.example.Data.NotificationsDbCommands;
import com.example.Models.Notification;
import com.example.Models.Person;

public class AdminInterface extends JFrame {

    private JButton btnShowManageDoctors, btnShowManagePatients, btnShowManageSchedule, btnNotifications;
    private JPanel mainPanel;
    private boolean disableMainWindow = false;
    private boolean disableDoctorWindow = false;
    private JLabel lbAdminName;
    private Person admin;
    private NotificationsDbCommands notfiyDb;
    private List<Notification> notifications;
   
    public AdminInterface(String title, Person admin) {
        super(title);
        this.admin = admin;
        
        
        
        // Set a modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        
        // Header panel with admin info
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(60, 141, 188));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        lbAdminName = new JLabel("Welcome, " + admin.getName(), JLabel.LEFT);
        lbAdminName.setFont(new Font("Arial", Font.BOLD, 16));
        lbAdminName.setForeground(Color.WHITE);
        headerPanel.add(lbAdminName, BorderLayout.WEST);
        
       
        notfiyDb = new NotificationsDbCommands();
        notifications = notfiyDb.getUserNotifications(admin.id);

        int unreadNotifications = 0;
        for(Notification notification : notifications )
        {
            if(!notification.isRead()) unreadNotifications++;
            
        }
        
        if(unreadNotifications == 0)  btnNotifications = new JButton("Notifications");
        else btnNotifications = new JButton("Notifications("+ unreadNotifications+")");
        
        btnNotifications.setFont(new Font("Arial", Font.BOLD, 12));
        btnNotifications.setBackground(new Color(41, 128, 185));
        btnNotifications.setForeground(Color.BLACK);
        btnNotifications.setFocusPainted(false);
        btnNotifications.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnNotifications.addActionListener(e -> showNotifications());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(60, 141, 188));
        
        
        JLabel titleLabel = new JLabel("Hospital Management System", JLabel.RIGHT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        rightPanel.add(titleLabel);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1, 0, 15));
        buttonsPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        buttonsPanel.setBackground(new Color(245, 245, 245));
        
        btnShowManageDoctors = createStyledButton("Manage Doctors", "doctors.png");
        btnShowManagePatients = createStyledButton("Manage Patients", "patients.png");
        btnShowManageSchedule = createStyledButton("Manage Schedules", "schedule.png");
        
        btnShowManageDoctors.addActionListener(e -> new AdminShowDoctors());
        btnShowManagePatients.addActionListener(e -> new AdminShowPatients());
        btnShowManageSchedule.addActionListener(e -> new AdminShowSchedules());
        
        buttonsPanel.add(btnShowManageDoctors);
        buttonsPanel.add(btnShowManagePatients);
        buttonsPanel.add(btnShowManageSchedule);
        buttonsPanel.add(btnShowManageSchedule);
        
        // Footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> System.exit(0));
        footerPanel.add(logoutButton);
        footerPanel.add(btnNotifications);
        
        // Add all components to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Setup JFrame properties
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void showNotifications() {
        JDialog notificationDialog = new JDialog(this, "Notifications", true);
        notificationDialog.setLayout(new BorderLayout());
        
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new GridLayout(notifications.size(), 1, 0, 1));
        notificationPanel.setBackground(Color.WHITE);
        
        if(notifications.size() == 0)
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            
            JLabel label = new JLabel("You have no notifications");
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(label, BorderLayout.CENTER);
            
            panel.setBackground(Color.WHITE);
            notificationPanel.add(panel);
        }
        for (Notification notification : notifications) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            
            JLabel label = new JLabel(notification.message);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(label, BorderLayout.CENTER);
            
            panel.setBackground(Color.WHITE);
            notificationPanel.add(panel);
            notfiyDb.markUserNotificationsAsRead(admin.id);
        }
        
        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        scrollPane.setBorder(null);
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(52, 152, 219));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> notificationDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(closeButton);
        
        notificationDialog.add(scrollPane, BorderLayout.CENTER);
        notificationDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        notificationDialog.setSize(400, 300);
        notificationDialog.setLocationRelativeTo(this);
        notificationDialog.setVisible(true);
    }
    
    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(44, 62, 80));
        button.setBackground(new Color(236, 240, 241));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            
        // Add icon if available
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconName));
            if (icon.getIconWidth() > 0) {
                button.setIcon(icon);
                button.setHorizontalAlignment(SwingConstants.LEFT);
            }
        } catch (Exception e) {
            // Icon not found, continue without icon
        }
        
        return button;
    }
}
