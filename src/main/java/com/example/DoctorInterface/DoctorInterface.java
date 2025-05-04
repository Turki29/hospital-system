package com.example.DoctorInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.example.Data.DescriptionsDbCommands;
import com.example.Data.NotificationsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Description;
import com.example.Models.Notification;
import com.example.Models.Person;

public class DoctorInterface extends JFrame {
    private JButton accessBtn, addMedBtn, cancelBtn, logOutBtn, viewEditButton, btnNotifications;
    private JComboBox appointments, patientPicker;

    private JLabel labelDocName, labelMeds, labelPatientName, labelAppointments;
    private JTextField textFieldMeds;
    private Person currentDoctor;
    private NotificationsDbCommands notifyDb;

    // UI Constants
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice Blue
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final int PADDING = 15;

    public DoctorInterface(String title, Person currentDoctor) {
        super(title);
        this.currentDoctor = currentDoctor;
        this.setLocation(200, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainPanel.setBackground(SECONDARY_COLOR);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 1, 10, 15));
        contentPanel.setBackground(SECONDARY_COLOR);
        
        // Patient Records Section
        JPanel recordsPanel = createRecordsPanel();
        
        // Appointments Section
        JPanel appointmentsPanel = createAppointmentsPanel();
        
        // Prescription Section
        JPanel prescriptionPanel = createPrescriptionPanel();
        
        contentPanel.add(recordsPanel);
        contentPanel.add(appointmentsPanel);
        contentPanel.add(prescriptionPanel);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setMinimumSize(new Dimension(600, 500));
        this.pack();
        this.setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        labelDocName = new JLabel("Doctor: " + currentDoctor.getName());
        labelDocName.setFont(HEADER_FONT);
        labelDocName.setForeground(Color.WHITE);
        
        panel.add(labelDocName, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Patient Records"
        ));
        panel.setBackground(SECONDARY_COLOR);
        
        accessBtn = new JButton("Access Patient Records");
        accessBtn.setFont(REGULAR_FONT);
        accessBtn.setBackground(PRIMARY_COLOR);
        accessBtn.setForeground(Color.WHITE);
        accessBtn.setFocusPainted(false);
        accessBtn.addActionListener(e -> new DoctorShowPatients(currentDoctor));
        
        panel.add(accessBtn, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Appointments"
        ));
        panel.setBackground(SECONDARY_COLOR);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(SECONDARY_COLOR);
        
        viewEditButton = new JButton("View/Edit Appointments");
        viewEditButton.setFont(REGULAR_FONT);
        viewEditButton.setBackground(PRIMARY_COLOR);
        viewEditButton.setForeground(Color.WHITE);
        viewEditButton.setFocusPainted(false);
        viewEditButton.addActionListener(e -> new DoctorShowAppointments(currentDoctor));
        
        notifyDb = new NotificationsDbCommands();
        List<Notification> notifications = notifyDb.getUserNotifications(currentDoctor.id);
        int unreadNotifications = 0;
        for (Notification notification : notifications) {
            if (!notification.isRead())
                unreadNotifications++;
        }
        
        btnNotifications = new JButton(unreadNotifications == 0 ? "Notifications" : "Notifications(" + unreadNotifications + ")");
        btnNotifications.setFont(REGULAR_FONT);
        btnNotifications.setBackground(PRIMARY_COLOR);
        btnNotifications.setForeground(Color.WHITE);
        btnNotifications.setFocusPainted(false);
        btnNotifications.addActionListener(e -> showNotificationsDialog(notifications));
        
        buttonPanel.add(viewEditButton);
        buttonPanel.add(btnNotifications);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Prescribe Medication"
        ));
        panel.setBackground(SECONDARY_COLOR);
        
        addMedBtn = new JButton("Add Prescription");
        addMedBtn.setFont(REGULAR_FONT);
        addMedBtn.setBackground(PRIMARY_COLOR);
        addMedBtn.setForeground(Color.WHITE);
        addMedBtn.setFocusPainted(false);
        addMedBtn.addActionListener(new AddMedActionListener());
        
        panel.add(addMedBtn, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(SECONDARY_COLOR);
        
        logOutBtn = new JButton("Log Out");
        logOutBtn.setFont(REGULAR_FONT);
        logOutBtn.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setFocusPainted(false);
        logOutBtn.addActionListener(e -> dispose());
        
        panel.add(logOutBtn);
        return panel;
    }
    
    private void showNotificationsDialog(List<Notification> notifications) {
        JDialog notificationDialog = new JDialog(this, "Notifications", true);
        notificationDialog.setLayout(new BorderLayout());

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBackground(Color.WHITE);
        
        if (notifications.isEmpty()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));

            JLabel label = new JLabel("You have no notifications");
            label.setFont(REGULAR_FONT);
            panel.add(label, BorderLayout.CENTER);
            panel.setBackground(Color.WHITE);
            notificationPanel.add(panel);
        } else {
            for (Notification notification : notifications) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));

                JLabel label = new JLabel(notification.message);
                label.setFont(REGULAR_FONT);
                if (!notification.read) {
                    label.setForeground(Color.RED);
                }
                panel.add(label, BorderLayout.CENTER);
                panel.setBackground(Color.WHITE);
                notificationPanel.add(panel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        notificationDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(event -> notificationDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.add(closeButton);
        notificationDialog.add(buttonPanel, BorderLayout.SOUTH);

        notificationDialog.setSize(400, 300);
        notificationDialog.setLocationRelativeTo(this);
        notificationDialog.setVisible(true);

        // Update the notification button after viewing
        notifyDb.markUserNotificationsAsRead(currentDoctor.id);
        btnNotifications.setText("Notifications");
    }

    // Listeners here

    public class AddMedActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new PrescribtionInterface("Prescribe Medication");
        }
    }

    // Prescription Interface
    public class PrescribtionInterface extends JFrame {
        private JLabel labelMed;
        private JComboBox cbDate;
        private String arrDate[] = { "Sat, 5:00PM 12/1/2025 ", "Mon, 5:30PM 12/3/2025" };
        private JButton confirmBtn, returnBtn;
        private UsersDbCommands userDb;
        private DescriptionsDbCommands descDb;

        public PrescribtionInterface(String title) {
            super(title);
            this.setSize(500, 350);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JPanel mainPanel = (JPanel) this.getContentPane();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
            mainPanel.setBackground(SECONDARY_COLOR);
            
            JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 15));
            formPanel.setBackground(SECONDARY_COLOR);
            
            // Patient Selection Panel
            JPanel patientPanel = new JPanel(new BorderLayout(10, 0));
            patientPanel.setBackground(SECONDARY_COLOR);
            patientPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Select Patient"
            ));
            
            labelPatientName = new JLabel("Patient:");
            labelPatientName.setFont(REGULAR_FONT);
            patientPicker = new JComboBox();
            patientPicker.setFont(REGULAR_FONT);
            
            patientPanel.add(labelPatientName, BorderLayout.WEST);
            patientPanel.add(patientPicker, BorderLayout.CENTER);
            
            // Medication Panel
            JPanel medicationPanel = new JPanel(new BorderLayout(10, 0));
            medicationPanel.setBackground(SECONDARY_COLOR);
            medicationPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Medication Details"
            ));
            
            labelMed = new JLabel("Medication:");
            labelMed.setFont(REGULAR_FONT);
            textFieldMeds = new JTextField(40);
            textFieldMeds.setFont(REGULAR_FONT);
            
            medicationPanel.add(labelMed, BorderLayout.WEST);
            medicationPanel.add(textFieldMeds, BorderLayout.CENTER);
            
            formPanel.add(patientPanel);
            formPanel.add(medicationPanel);
            
            // Button Panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(SECONDARY_COLOR);
            
            confirmBtn = new JButton("Confirm");
            confirmBtn.setFont(REGULAR_FONT);
            confirmBtn.setBackground(new Color(40, 167, 69)); // Bootstrap success green
            confirmBtn.setForeground(Color.WHITE);
            confirmBtn.setFocusPainted(false);
            
            cancelBtn = new JButton("Cancel");
            cancelBtn.setFont(REGULAR_FONT);
            cancelBtn.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
            cancelBtn.setForeground(Color.WHITE);
            cancelBtn.setFocusPainted(false);
            
            buttonPanel.add(confirmBtn);
            buttonPanel.add(cancelBtn);
            
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Initialize database connection and populate patient list
            userDb = new UsersDbCommands();
            descDb = new DescriptionsDbCommands();
            
            final List<Person> doctorsPatients = userDb.getDoctorsPatients(currentDoctor.id);
            for (Person patient : doctorsPatients) {
                patientPicker.addItem(patient.name);
            }
            
            cancelBtn.addActionListener(e -> dispose());
            
            confirmBtn.addActionListener(e -> {
                if (textFieldMeds.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please write a prescription", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedPatientName = (String) patientPicker.getSelectedItem();
                Person selectedPatient = null;
                for (Person patient : doctorsPatients) {
                    if (patient.name.equals(selectedPatientName)) {
                        selectedPatient = patient;
                        break;
                    }
                }

                if (selectedPatient == null) {
                    JOptionPane.showMessageDialog(null, "Patient not selected or not found in your patients list", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int prescriptionId = descDb.insertDescription(new Description(-1, textFieldMeds.getText(), selectedPatient.id));
                JOptionPane.showMessageDialog(null,
                        "Prescription with ID " + prescriptionId + " for patient " + selectedPatientName +
                                " added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
            
            this.pack();
            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        // DoctorInterface DI = new DoctorInterface("Home Page");
    }
}