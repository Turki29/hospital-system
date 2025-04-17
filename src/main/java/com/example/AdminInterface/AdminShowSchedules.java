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
    JTextField tfDoctorID, tfName, tfEmail, tfPhone;
    JButton btnSearch, btnAdd;
    JTable table;
    DefaultTableModel tableModel;
    AppointmentsDbCommands appointmentDb;
    private List<Appointment> appointmentsList = new ArrayList<>();

    public AdminShowSchedules() {
        setTitle("Schedule Manager");
        appointmentDb = new AppointmentsDbCommands();

        JLabel lblID = new JLabel("ID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblPhone = new JLabel("Phone:");

        tfDoctorID = new JTextField(10);
        tfName = new JTextField(10);
        tfEmail = new JTextField(10);
        tfPhone = new JTextField(10);

        btnAdd = new JButton("Add");
        btnSearch = new JButton("Search");
        // search functionality
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder queryBuilder = new StringBuilder("role = 'doctor'");
                

                if (!tfDoctorID.getText().trim().isEmpty()) {
                    queryBuilder.append(" AND id = '").append(tfDoctorID.getText().trim()).append("'");
                }

                if (!tfName.getText().trim().isEmpty()) {
                    queryBuilder.append(" AND name LIKE '%").append(tfName.getText().trim()).append("%'");
                }

                if (!tfEmail.getText().trim().isEmpty()) {
                    queryBuilder.append(" AND email LIKE '%").append(tfEmail.getText().trim()).append("%'");
                }

                if (!tfPhone.getText().trim().isEmpty()) {
                    queryBuilder.append(" AND phoneNumber LIKE '%").append(tfPhone.getText().trim()).append("%'");
                }

                String query = queryBuilder.toString();
                appointmentsList = userDb.getUsers(query);
                // Clear the existing table data
                tableModel.setRowCount(0);

                // Populate the table with the search results
                if (appointmentsList != null && !appointmentsList.isEmpty()) {
                    for (Person doctor : appointmentsList) {
                        tableModel.addRow(new Object[] {
                                doctor.id,
                                doctor.name,
                                doctor.email,
                                doctor.phoneNumber,
                                doctor.password,
                                doctor.role,
                                doctor.clinicId
                        });
                    }
                }
                // Refresh the table display
                table.repaint();
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
        inputPanel.add(tfDoctorID);
        inputPanel.add(lblName);
        inputPanel.add(tfName);
        inputPanel.add(lblEmail);
        inputPanel.add(tfEmail);
        inputPanel.add(lblPhone);
        inputPanel.add(tfPhone);
        inputPanel.add(btnSearch);
        inputPanel.add(btnAdd);

        String[] columns = {"ID","Clinic","Doctor","Patient","Day","Time"};

        // Initialize the table model and table
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Populate the table with doctor data
        // Assuming there's a method to get doctors from the database
        appointmentsList = appointmentDb.getAppointments();
        if(appointmentsList.isEmpty()) btnAdd.setText("empty");
        else btnAdd.setText("not empty");
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
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && appointmentsList != null) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    // for (Person doctor : appointmentsList) {
                    //     if (doctor.id == id) {
                    //         new ShowDoctorDetails(doctor);
                    //         break;
                    //     }
                    // }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(800, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
