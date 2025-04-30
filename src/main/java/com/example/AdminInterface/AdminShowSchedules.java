package com.example.AdminInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import com.example.Data.AppointmentsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Appointment;
import com.example.Models.Person;
import java.util.List;
import java.util.ArrayList;
public class AdminShowSchedules extends JFrame{
    JTextField tfID, tfDoctorName, tfPatientName, tfDate, tfTime;
    JButton btnSearch, btnAdd;
    JTable table;
    DefaultTableModel tableModel;
    AppointmentsDbCommands appointmentDb;
    UsersDbCommands userDb;
    private List<Appointment> appointmentsList = new ArrayList<>();

    public AdminShowSchedules() {
        setTitle("Schedule Manager");
        appointmentDb = new AppointmentsDbCommands();
        userDb = new UsersDbCommands();
        JLabel lblID = new JLabel("ID:");
        JLabel lblDoctorName = new JLabel("Dr.Name:");
        JLabel lblPatientName = new JLabel("P.Name:");
        JLabel lblDay = new JLabel("Day:");
        JLabel lblTime = new JLabel("Time:");

        tfID = new JTextField(10);
        tfDoctorName = new JTextField(10);
        tfPatientName = new JTextField(10);
        tfDate = new JTextField(10);
        tfTime = new JTextField(10);

        btnAdd = new JButton("Add");
        btnSearch = new JButton("Search");
        
        // search functionality
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String idFilter = tfID.getText().trim().toLowerCase();
            String doctorFilter = tfDoctorName.getText().trim().toLowerCase();
            String patientFilter = tfPatientName.getText().trim().toLowerCase();
            String dayFilter = tfDate.getText().trim().toLowerCase();
            String timeFilter = tfTime.getText().trim().toLowerCase();
            
            // Clear the existing table data
            tableModel.setRowCount(0);

            // Populate the table with filtered results
            if (appointmentsList != null) {
                for (Appointment appointment : appointmentsList) {
                String id = String.valueOf(appointment.getId()).toLowerCase();
                String doctor = appointment.getDoctorName().toLowerCase();
                String patient = appointment.getPatientName().toLowerCase();
                String day = appointment.getDay().toLowerCase();
                String time = appointment.getTime().toLowerCase();
                
                
                // Check if the appointment matches all non-empty filters
                if ((idFilter.isEmpty() || id.contains(idFilter)) &&
                    (doctorFilter.isEmpty() || doctor.contains(doctorFilter)) &&
                    (patientFilter.isEmpty() || patient.contains(patientFilter)) &&
                    (dayFilter.isEmpty() || day.contains(dayFilter)) &&
                    (timeFilter.isEmpty() || time.contains(timeFilter))) {
                    
                    tableModel.addRow(new Object[] {
                    appointment.getId(),
                    appointment.getClinicName(),
                    appointment.getDoctorName(),
                    appointment.getPatientName(),
                    appointment.getDay(),
                    appointment.getTime()
                    });
                }
                }
            }
            }
        });
        
        // add functionality
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                new AdminShowAddDoctorWindow();
            }
        });
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(lblID);
        inputPanel.add(tfID);
        inputPanel.add(lblDoctorName);
        inputPanel.add(tfDoctorName);
        inputPanel.add(lblPatientName);
        inputPanel.add(tfPatientName);
        inputPanel.add(lblDay);
        inputPanel.add(tfDate);
        inputPanel.add(lblTime);
        inputPanel.add(tfTime);
        inputPanel.add(btnSearch);
        inputPanel.add(btnAdd);

        String[] columns = {"ID","Clinic","Doctor","Patient","Day","Time"};

        // Initialize the table model and table
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

      
        appointmentsList = appointmentDb.getAppointments();
        
        if (appointmentsList != null) {
            for (Appointment appointment : appointmentsList) {
                tableModel.addRow(new Object[] {
                    appointment.getId(),
                        appointment.getClinicName(),
                        appointment.getDoctorName(),
                        appointment.getPatientName(),
                        appointment.getDay(),
                        appointment.getTime()
                });
            }
        }
        

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel btnsPanel = new JPanel(new FlowLayout());
        JButton btnReshcedule = new JButton("Reschedule Appointment");
        JButton btnCancel = new JButton("Cancel Appointment");
        
        btnReshcedule.addActionListener(e -> rescheduleSelectedAppointment());
        btnCancel.addActionListener(e -> cancelSelectedAppointment());

        btnsPanel.add(btnCancel);
        btnsPanel.add(btnReshcedule);
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnsPanel, BorderLayout.SOUTH);
        

        setSize(1000, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentDb.getAppointments();

        for (Appointment appointment : appointments) {
            tableModel.addRow(new Object[] {
                appointment.getId(),
                appointment.getClinicName(),
                appointment.getDoctorName(),
                appointment.getPatientName(),
                appointment.getDay(),
                appointment.getTime()
                });
        }
    }

    private void cancelSelectedAppointment() {
        int selectedRow = table.getSelectedRow();
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
        int selectedRow = table.getSelectedRow();
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

class ShowDoctorDetails extends JFrame {
    JTextField tfName, tfPhone, tfEmail;
    JButton btnEdit, btnSave;
    private Person doctor;
    UsersDbCommands userDb;

    public ShowDoctorDetails(Person doctor) {
        userDb = new UsersDbCommands();
        this.doctor = doctor;
        setTitle("Doctor Details");

        JLabel lblID = new JLabel("Doctor ID: " + doctor.id);
        JLabel lblName = new JLabel("Name:");
        JLabel lblPhone = new JLabel("Phone:");
        JLabel lblEmail = new JLabel("Email:");

        tfName = new JTextField(doctor.name, 20);
        tfPhone = new JTextField(doctor.phoneNumber, 20);
        tfEmail = new JTextField(doctor.email, 20);

        tfName.setEditable(false);
        tfPhone.setEditable(false);
        tfEmail.setEditable(false);

        btnEdit = new JButton("Edit");
        btnSave = new JButton("Save");
        btnSave.setEnabled(false);
        

        btnEdit.addActionListener(e -> enableEditing());

        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.add(lblID);
        detailsPanel.add(new JLabel());
        detailsPanel.add(lblName);
        detailsPanel.add(tfName);
        detailsPanel.add(lblPhone);
        detailsPanel.add(tfPhone);
        detailsPanel.add(lblEmail);
        detailsPanel.add(tfEmail);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);
        
        

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Person userOldInfo = userDb.getUser(doctor.id);
                userDb.UpdateUser(
                        new Person(doctor.id, tfName.getText(), tfEmail.getText(), tfPhone.getText(),
                                doctor.password, doctor.role, doctor.clinicId));
                disableEditing();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void enableEditing() {
        tfName.setEditable(true);
        tfPhone.setEditable(true);
        tfEmail.setEditable(true);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
    }

    private void disableEditing() {
        tfName.setEditable(false);
        tfPhone.setEditable(false);
        tfEmail.setEditable(false);
        btnEdit.setEnabled(true);
        btnSave.setEnabled(false);
    }

    

}
