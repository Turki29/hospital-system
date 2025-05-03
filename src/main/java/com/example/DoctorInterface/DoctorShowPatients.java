package com.example.DoctorInterface;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseEvent;

import com.example.AdminInterface.AdminShowAddPatientWindow;
import com.example.Data.PatientsRecordsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Person;

public class DoctorShowPatients extends JFrame //implements ActionListener 
{
   JTextField tfPatientID, tfName, tfEmail, tfPhone;
    JButton btnSearch, btnAdd;
    JTable table;
    DefaultTableModel tableModel;
    UsersDbCommands userDb;
    private java.util.List<Person> patientsList = new java.util.ArrayList<>();
    private Person currentDoctor;

    public DoctorShowPatients(Person currentDoctor) {
        this.currentDoctor = currentDoctor;    
        userDb = new UsersDbCommands();
     patientsList = userDb.getDoctorsPatients(currentDoctor.getId());

        JLabel lblID = new JLabel("ID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblPhone = new JLabel("Phone:");

        tfPatientID = new JTextField(10);
        tfName = new JTextField(10);
        tfEmail = new JTextField(10);
        tfPhone = new JTextField(10);

        btnAdd = new JButton("Add");
        btnSearch = new JButton("Search");
        // search functionality
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the existing table data
                tableModel.setRowCount(0);

                // Create a filtered list based on search criteria
                java.util.List<Person> filteredList = new java.util.ArrayList<>();
                
                for (Person patient : patientsList) {
                    boolean matches = true;
                    
                    // Check ID if provided
                    if (!tfPatientID.getText().trim().isEmpty()) {
                        try {
                            int searchId = Integer.parseInt(tfPatientID.getText().trim());
                            if (patient.id != searchId) {
                                matches = false;
                            }
                        } catch (NumberFormatException ex) {
                            matches = false;
                        }
                    }
                    
                    // Check name if provided
                    if (matches && !tfName.getText().trim().isEmpty()) {
                        if (!patient.name.toLowerCase().contains(tfName.getText().trim().toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    // Check email if provided
                    if (matches && !tfEmail.getText().trim().isEmpty()) {
                        if (!patient.email.toLowerCase().contains(tfEmail.getText().trim().toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    // Check phone if provided
                    if (matches && !tfPhone.getText().trim().isEmpty()) {
                        if (!patient.phoneNumber.contains(tfPhone.getText().trim())) {
                            matches = false;
                        }
                    }
                    
                    if (matches) {
                        filteredList.add(patient);
                    }
                }
                
                // Populate the table with the search results or full list if all fields are empty
                java.util.List<Person> displayList = (tfPatientID.getText().trim().isEmpty() && 
                                                     tfName.getText().trim().isEmpty() && 
                                                     tfEmail.getText().trim().isEmpty() && 
                                                     tfPhone.getText().trim().isEmpty()) 
                                                     ? patientsList : filteredList;
                                                     
                for (Person patient : displayList) {
                    tableModel.addRow(new Object[] {
                            patient.id,
                            patient.name,
                            patient.email,
                            patient.phoneNumber,
                            patient.password,
                            patient.phoneNumber
                    });
                }
                
                // Refresh the table display
                table.repaint();
            }
        });
        
        // add functionality
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                new AdminShowAddPatientWindow();
            }
        });
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(lblID);
        inputPanel.add(tfPatientID);
        inputPanel.add(lblName);
        inputPanel.add(tfName);
        inputPanel.add(lblEmail);
        inputPanel.add(tfEmail);
        inputPanel.add(lblPhone);
        inputPanel.add(tfPhone);
        inputPanel.add(btnSearch);
        inputPanel.add(btnAdd);

        String[] columns = { "ID", "Name", "Email", "Phone", "Password", "Phone"};

        // Initialize the table model and table
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Populate the table with patient data
        // Assuming there's a method to get patient from the database
        
        if (patientsList != null) {
            for (Person patient : patientsList) {
                tableModel.addRow(new Object[] {
                        patient.id,
                        patient.name,
                        patient.email,
                        patient.phoneNumber,
                        patient.password,
                        patient.phoneNumber
                });
            }
        }
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && patientsList != null) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    for (Person patient : patientsList) {
                        if (patient.id == id) {
                            new ShowPatientDetails(patient);
                            break;
                        }
                    }
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


class ShowPatientDetails extends JFrame {
    JTextField tfName, tfPhone, tfEmail;
    JButton btnEdit, btnSave, btnViewRecord;
    private Person patient;
    UsersDbCommands userDb;
    PatientsRecordsDbCommands recordDb;
    JLabel lblRecordStatus;

    public ShowPatientDetails(Person patient) {
        userDb = new UsersDbCommands();
        recordDb = new PatientsRecordsDbCommands();
        this.patient = patient;
        setTitle("Patient Details");

        JLabel lblID = new JLabel("Patient ID: " + patient.id);
        JLabel lblName = new JLabel("Name:");
        JLabel lblPhone = new JLabel("Phone:");
        JLabel lblEmail = new JLabel("Email:");
        lblRecordStatus = new JLabel();

        tfName = new JTextField(patient.name, 20);
        tfPhone = new JTextField(patient.phoneNumber, 20);
        tfEmail = new JTextField(patient.email, 20);

        
        tfName.setEditable(false);
        tfPhone.setEditable(false);
        tfEmail.setEditable(false);

        btnEdit = new JButton("Edit");
        btnSave = new JButton("Save");
        btnViewRecord = new JButton("View Record");
        btnSave.setEnabled(false);
        
        // Check if patient has a record
        var record = recordDb.getRecordByUserId(patient.id);
        
        if (record.getId() == -1) {
            lblRecordStatus.setText("No record found");
            btnViewRecord.setEnabled(false);
        } else {
            lblRecordStatus.setText("Patient has a medical record");
            btnViewRecord.setEnabled(true);
        }

        btnEdit.addActionListener(e -> enableEditing());
        
        btnViewRecord.addActionListener(e -> {
            if (record.getId() != -1) {
                showPatientRecord(record);
            }
        });

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.add(lblID);
        detailsPanel.add(new JLabel());
        detailsPanel.add(lblName);
        detailsPanel.add(tfName);
        detailsPanel.add(lblPhone);
        detailsPanel.add(tfPhone);
        detailsPanel.add(lblEmail);
        detailsPanel.add(tfEmail);
        detailsPanel.add(lblRecordStatus);
        detailsPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnViewRecord);
        
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Person userOldInfo = userDb.getUser(patient.id);
                userDb.UpdateUser(new Person(patient.id, tfName.getText(), tfEmail.getText(), tfPhone.getText(),
                                patient.password, patient.role, patient.clinicId));
                disableEditing();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void showPatientRecord(com.example.Models.PatientRecord record) {
        JFrame recordFrame = new JFrame("Patient Record");
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panel.add(new JLabel("Record ID:"));
        panel.add(new JLabel(String.valueOf(record.getId())));
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(new JLabel(String.valueOf(record.getUserId())));
        
        panel.add(new JLabel("Weight:"));
        panel.add(new JLabel(String.valueOf(record.getWeight()) + " kg"));
        
        panel.add(new JLabel("Height:"));
        panel.add(new JLabel(String.valueOf(record.getHeight()) + " cm"));
        
        panel.add(new JLabel("Blood Pressure:"));
        panel.add(new JLabel(record.getBloodPressure()));
        
        panel.add(new JLabel("Sugar:"));
        panel.add(new JLabel(String.valueOf(record.getSugar()) + " mg/dL"));
        
        panel.add(new JLabel("Longterm Disease:"));
        panel.add(new JLabel(record.getLongtermDisease()));
        
        recordFrame.add(panel);
        recordFrame.setSize(400, 300);
        recordFrame.setLocationRelativeTo(this);
        recordFrame.setVisible(true);
        recordFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

