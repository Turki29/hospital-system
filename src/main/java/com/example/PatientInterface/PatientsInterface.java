package com.example.PatientInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.example.Data.AppointmentsDbCommands;
import com.example.Data.NotificationsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Appointment;
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

        btnBookAppointment.addActionListener(new Book());
        btnViewAppointments.addActionListener(new ViewAppointments());
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

        btnNotifications = createStyledButton(unreadNotifications == 0 ? 
                "Notifications" : "Notifications (" + unreadNotifications + ")");
        
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

    // Keep existing inner classes unchanged
    public class Book extends JFrame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setTitle("Book Appointment");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            AppointmentsDbCommands appointmentDb = new AppointmentsDbCommands();
            UsersDbCommands userDb = new UsersDbCommands();

            List<String> clinicsList = appointmentDb.getClinics();
            JComboBox<String> clinicsCombo = new JComboBox<>(clinicsList.toArray(new String[0]));
            JComboBox<String> doctorCombo = new JComboBox<>();

            JComboBox<String> dayCombo = new JComboBox<>(new String[] {
                    "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
            });
            JComboBox<String> timeCombo = new JComboBox<>(new String[] {
                    "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM"
            });

            List<Person> doctors = new ArrayList<>();
            doctors = userDb.getDoctors();
            for (Person doctor : doctors) {
                doctorCombo.addItem(doctor.name);
            }

            JTextField searchField = new JTextField(10);
            JButton btnSearch = new JButton("Search");

            btnSearch.addActionListener(ev -> {
                String keyword = searchField.getText().trim();
                List<Person> results = userDb.getUsers("name LIKE '%" + keyword + "%' AND role = 'doctor'");
                doctorCombo.removeAllItems();
                for (Person doc : results) {
                    doctorCombo.addItem(doc.name);
                }
            });

            clinicsCombo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    String selectedDepartment = (String) clinicsCombo.getSelectedItem();
                    int clinicId = appointmentDb.getClinicIdByName(selectedDepartment);

                    List<Person> doctors = appointmentDb.getDoctorsByClinic(clinicId);
                    String docotrs = clinicId + " ";
                    doctorCombo.removeAllItems();
                    for (Person doctor : doctors) {
                        doctorCombo.addItem(doctor.name);
                        docotrs += doctor.name + "  ";

                    }

                    JOptionPane.showMessageDialog(null, docotrs);

                }

            });

            JButton btnBook = new JButton("Book Appointment");
            JButton btnCancel = new JButton("Cancel");

            btnBook.addActionListener(ev -> {
                String doctorName = (String) doctorCombo.getSelectedItem();
                String department = (String) clinicsCombo.getSelectedItem();
                String day = (String) dayCombo.getSelectedItem();
                String time = (String) timeCombo.getSelectedItem();

                if (doctorName == null || day == null || time == null) {
                    JOptionPane.showMessageDialog(this, "Please select all fields.");
                    return;
                }

                int doctorId = userDb.getUserIdByName(doctorName);
                int clinicId = appointmentDb.getClinicIdByName(department);
                boolean isTaken = appointmentDb.isAppointmentSlotTaken(doctorId, day, time);

                if (!isTaken) {
                    Appointment a = new Appointment(0, patient.getId(), doctorId, clinicId, day, time);
                    boolean success = appointmentDb.insertAppointment(a);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Booking failed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "This slot is already taken. Please choose another time.");
                }
            });

            btnCancel.addActionListener(ev -> dispose());

            JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
            panel.add(new JLabel("Search Doctor:"));
            panel.add(searchField);
            panel.add(new JLabel(""));
            panel.add(btnSearch);
            panel.add(new JLabel("Department:"));
            panel.add(clinicsCombo);
            panel.add(new JLabel("Doctor:"));
            panel.add(doctorCombo);
            panel.add(new JLabel("Day:"));
            panel.add(dayCombo);
            panel.add(new JLabel("Time:"));
            panel.add(timeCombo);
            panel.add(btnBook);
            panel.add(btnCancel);

            add(panel);
            setVisible(true);
        }
    }

    public class ViewAppointments extends JFrame implements ActionListener {

        private JTable appointmentsTable;
        private DefaultTableModel tableModel;
        private JButton cancelBtn, rescheduleBtn, backBtn, btnRefresh;
        private AppointmentsDbCommands appointmentDb;
        private UsersDbCommands userDb;

        private Person currentDoctor;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Initialize database commands each time the view is opened
            userDb = new UsersDbCommands();
            appointmentDb = new AppointmentsDbCommands();

            setLayout(new BorderLayout());
            setSize(700, 400);
            setLocation(400, 200);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Table model
            String[] columnNames = { "Appointment ID", "Doctor Name", "Date", "Time" };
            tableModel = new DefaultTableModel(columnNames, 0);
            appointmentsTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(appointmentsTable);
            add(scrollPane, BorderLayout.CENTER);

            // Fetch and show data
            loadAppointments();

            // Buttons
            JPanel buttonPanel = new JPanel();
            cancelBtn = new JButton("Cancel Appointment");
            rescheduleBtn = new JButton("Reschedule");
            backBtn = new JButton("Back");
            btnRefresh = new JButton("Refresh");

            btnRefresh.addActionListener(e2 -> {
            // Reinitialize database connections before refreshing
            userDb = new UsersDbCommands();
            appointmentDb = new AppointmentsDbCommands();
            loadAppointments();
            });

            buttonPanel.add(cancelBtn);
            buttonPanel.add(rescheduleBtn);
            buttonPanel.add(btnRefresh);
            buttonPanel.add(backBtn);
            add(buttonPanel, BorderLayout.SOUTH);

            // Listeners
            cancelBtn.addActionListener(ee -> cancelSelectedAppointment());
            rescheduleBtn.addActionListener(ee -> rescheduleSelectedAppointment());
            backBtn.addActionListener(ee -> dispose());
            
            setVisible(true);
        }

        private void loadAppointments() {
            tableModel.setRowCount(0);
            // Get fresh data from database
            List<Appointment> appointments = appointmentDb.getAppointmentsForPatient(patient.id);
            
            for (Appointment appointment : appointments) {
            Person doctor = userDb.getUser(appointment.getDoctorId());
            String doctorName = doctor.getName();
            tableModel.addRow(new Object[] {
                appointment.getId(),
                doctorName,
                appointment.getDay(),
                appointment.getTime()
            });
            }
        }

        private void cancelSelectedAppointment() {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an appointment.");
                return;
            }

            int apptId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userDb.cancelAppointment(apptId);
                loadAppointments();
            }
        }

        private void rescheduleSelectedAppointment() {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an appointment.");
                return;
            }

            int apptId = (int) tableModel.getValueAt(selectedRow, 0);

            // Panel for custom input dialog
            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.add(new JLabel("Select Day:"));
            String[] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
            JComboBox<String> dayComboBox = new JComboBox<>(daysOfWeek);
            panel.add(dayComboBox);

            panel.add(new JLabel("Select Hour (24h):"));
            JComboBox<String> hourComboBox = new JComboBox<>();
            for (int i = 0; i < 24; i++) {
                hourComboBox.addItem(i + "");
            }
            panel.add(hourComboBox);

            panel.add(new JLabel("Select Minutes:"));
            JComboBox<String> minuteComboBox = new JComboBox<>();
            for (int i = 0; i < 60; i += 5) { // Increment by 5 minutes
                minuteComboBox.addItem(i + "");
            }
            panel.add(minuteComboBox);

            int result = JOptionPane.showConfirmDialog(this, panel, "Reschedule Appointment",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newDate = (String) dayComboBox.getSelectedItem();
                int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
                String minutes = (String) minuteComboBox.getSelectedItem();

                String period = (hour < 12) ? "AM" : "PM";
                int displayHour = (hour == 0) ? 12 : (hour > 12) ? hour - 12 : hour;
                String newTime = displayHour + ":" + minutes + " " + period;

                userDb.rescheduleAppointment(apptId, newDate, newTime);
                loadAppointments();
            }
        }
    }
}