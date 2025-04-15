package com.example.AdminInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.events.MouseEvent;

import com.example.Data.UsersDbCommands;
import com.example.Models.Person;

public class AdminShowPatients extends JFrame //implements ActionListener 
{
   JTextField tfPatientID, tfName, tfEmail, tfPhone;
    JButton btnSearch, btnAdd;
    JTable table;
    DefaultTableModel tableModel;
    UsersDbCommands userDb;
    private java.util.List<Person> patientsList = new java.util.ArrayList<>();

    public AdminShowPatients() {
        setTitle("Patients Manager");
        userDb = new UsersDbCommands();

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
                String query = "role = 'patient'";
                StringBuilder queryBuilder = new StringBuilder(query);
                

                if (!tfPatientID.getText().trim().isEmpty()) {
                    queryBuilder.append(" AND id = '").append(tfPatientID.getText().trim()).append("'");
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

                query = queryBuilder.toString();
                patientsList = userDb.getUsers(query);
                // Clear the existing table data
                tableModel.setRowCount(0);

                // Populate the table with the search results
                if (patientsList != null && !patientsList.isEmpty()) {
                    for (Person patient : patientsList) {
                        tableModel.addRow(new Object[] {
                                patient.id,
                                patient.name,
                                patient.email,
                                patient.phoneNumber,
                                patient.password,
                                patient.role,
                                patient.clinicId
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

        String[] columns = { "ID", "Name", "Email", "Phone", "Password", "Role"};

        // Initialize the table model and table
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Populate the table with patient data
        // Assuming there's a method to get patient from the database
        patientsList = userDb.getUsers("role = 'patient'"); // Replace with actual method to get patient
        if (patientsList != null) {
            for (Person patient : patientsList) {
                tableModel.addRow(new Object[] {
                        patient.id,
                        patient.name,
                        patient.email,
                        patient.phoneNumber,
                        patient.password,
                        patient.role,
                        patient.clinicId
                });
            }
        }
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && patientsList != null) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    for (Person patient : patientsList) {
                        if (patient.id == id) {
                            // new ShowDoctorDetails(patient);
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





