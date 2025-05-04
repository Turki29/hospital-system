package com.example.PatientInterface;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.Data.AppointmentsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Appointment;
import com.example.Models.Person;

public class Book extends JFrame implements ActionListener {


    private Person patient;

    public Book ( Person patient)
    {
        this.patient = patient;
    }


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
               
                doctorCombo.removeAllItems();
                for (Person doctor : doctors) {
                    doctorCombo.addItem(doctor.name);
                   

                }

                

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

