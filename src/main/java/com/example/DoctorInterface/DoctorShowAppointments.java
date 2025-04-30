package com.example.DoctorInterface;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.example.Data.UsersDbCommands;
import com.example.Models.Appointment;
import com.example.Models.Person;

public class DoctorShowAppointments extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton cancelBtn, rescheduleBtn, backBtn;
    private UsersDbCommands db;
    private Person currentDoctor;

    public DoctorShowAppointments(Person doctor) {
        super("Doctor Appointments");
        this.currentDoctor = doctor;
        this.db = new UsersDbCommands();

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
        cancelBtn.addActionListener(e -> cancelSelectedAppointment());
        rescheduleBtn.addActionListener(e -> rescheduleSelectedAppointment());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = db.getDoctorsAppointments(currentDoctor.getId());

        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                a.getId(),
                a.getPatientName(),
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
            db.cancelAppointment(apptId);
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

            db.rescheduleAppointment(apptId, newDate, newTime);
            loadAppointments();
        }
    }
}