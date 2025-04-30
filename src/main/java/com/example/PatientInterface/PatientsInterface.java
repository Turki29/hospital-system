package com.example.PatientInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        lbPatientName = new JLabel("Patient Name: " + patient.getName(), JLabel.LEFT);
        lbDashboard = new JLabel("Patient Dashboard", JLabel.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        btnBookAppointment = new JButton("Book Appointment");
        btnViewAppointments = new JButton("View Appointments");
        btnExit = new JButton("Exit");

        btnBookAppointment.addActionListener(new Book());
        btnViewAppointments.addActionListener(new ViewAppointments());
        btnExit.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        notfiyDb = new NotificationsDbCommands();

        List<Notification> notifications = notfiyDb.getUserNotifications(patient.id);

        int unreadNotifications = 0;
        for(Notification notification : notifications )
        {
            if(!notification.isRead()) unreadNotifications++;
            
        }
        
        if(unreadNotifications == 0)  btnNotifications = new JButton("Notifications");
        else btnNotifications = new JButton("Notifications("+ unreadNotifications+")");

    btnNotifications.addActionListener(e -> {
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
            if(!notification.read)
            {
                label.setForeground(Color.RED);
            }
            panel.add(label, BorderLayout.CENTER);
            
            panel.setBackground(Color.WHITE);
            notificationPanel.add(panel);
            
            
            
        }
        
        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        notificationDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> notificationDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        notificationDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        notificationDialog.setSize(400, 300);
        notificationDialog.setLocationRelativeTo(this);
        notificationDialog.setVisible(true);
        
        // Update the notification button after viewing
        notfiyDb.markUserNotificationsAsRead(patient.id);
        btnNotifications.setText("Notifications");
    });


        buttonsPanel.add(btnBookAppointment);
        buttonsPanel.add(btnViewAppointments);
        buttonsPanel.add(btnNotifications);
        buttonsPanel.add(btnExit);

        mainPanel.add(lbPatientName, BorderLayout.NORTH);
        mainPanel.add(lbDashboard, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setVisible(true);
    }

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
            for(Person doctor : doctors)
            {
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

            clinicsCombo.addActionListener( new ActionListener() {
             
                public void actionPerformed(ActionEvent e)
                {
                    
                        String selectedDepartment = (String) clinicsCombo.getSelectedItem();
                        int clinicId = appointmentDb.getClinicIdByName(selectedDepartment);
                        
                        List<Person> doctors = appointmentDb.getDoctorsByClinic(clinicId );

                        

                        doctorCombo.removeAllItems();
                        for (Person doctor : doctors) {
                            doctorCombo.addItem(doctor.name);
                        }
                    
                }
                
            } );

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
        private JButton cancelBtn, rescheduleBtn, backBtn;
        private AppointmentsDbCommands appointmentDb;
        private UsersDbCommands userDb;
        
        private Person currentDoctor;

        @Override
        public void actionPerformed(ActionEvent e) {
        

    
        
        
        userDb = new UsersDbCommands();
        this.appointmentDb = new AppointmentsDbCommands();

        setLayout(new BorderLayout());
        setSize(700, 400);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table model
        String[] columnNames = {"Appointment ID", "Patient Name", "Date", "Time"};
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

        buttonPanel.add(cancelBtn);
        buttonPanel.add(rescheduleBtn);
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
        List<Appointment> appointments = appointmentDb.getAppointmentsForPatient(patient.id);

        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                a.getId(),
                patient.name,
                a.getDay(),
                a.getTime()
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
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm", JOptionPane.YES_NO_OPTION);
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
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
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

        int result = JOptionPane.showConfirmDialog(this, panel, "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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