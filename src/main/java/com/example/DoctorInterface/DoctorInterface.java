package com.example.DoctorInterface;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import com.example.Data.DescriptionsDbCommands;
import com.example.Data.UsersDbCommands;
import com.example.Models.Description;
import com.example.Models.Patient;
import com.example.Models.Person;

public class DoctorInterface extends JFrame {
    private JButton AccessBtn, AddMedBtn, Can1Btn, LogOutBtn, VEButton;
    private JComboBox Apptmnts, PatPick;

    private JLabel LDocName, PMeds, PName, LApptmnts;
    private JTextField TFPMeds;
    private Person currentDoctor;

    public DoctorInterface(String title, Person currentDoctor) {
        super(title);
        this.currentDoctor = currentDoctor;
        this.setLocation(200, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel MainP = (JPanel) this.getContentPane();
        MainP.setLayout(new BoxLayout(MainP, BoxLayout.Y_AXIS));

        JPanel P1 = new JPanel(new GridLayout(2, 1));
        JPanel P11 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        LDocName = new JLabel("Doctor: " + currentDoctor.getName());
        AccessBtn = new JButton("Access Patient Records");
        AccessBtn.addActionListener(e -> new DoctorShowPatients(currentDoctor));
        P11.add(LDocName);
        P11.add(AccessBtn);
        P1.add(P11);
        //
        JPanel P2 = new JPanel(new GridLayout(2, 1));
        JPanel P21 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel P22 = new JPanel();

        LApptmnts = new JLabel("Appointments: ");
        VEButton = new JButton("View/Edit Appointments");
        VEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorShowAppointments(currentDoctor);
            }
        });
        P21.add(LApptmnts, BorderLayout.NORTH);
        P22.add(VEButton);
        P2.add(P21);
        P2.add(P22);
        //
        JPanel P3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PMeds = new JLabel("Prescribe Medication : ");
        AddMedBtn = new JButton("Add Perscribtion");
        AddMedBtn.addActionListener(new AddMedActionListener());
        P3.add(PMeds);
        P3.add(AddMedBtn);
        //
        JPanel P4 = new JPanel();
        LogOutBtn = new JButton("Log Out");
        LogOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        P4.add(LogOutBtn, BorderLayout.SOUTH);
        //

        MainP.add(P1);

        MainP.add(P2);

        MainP.add(P3);

        MainP.add(P4);

        this.pack();
        this.show();
    }

    // Listeners here

    public class AddMedActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PrescribtionInterface("Prescribe Medication");

        }

    }

    //
    // Prescribtion Interface
    public class PrescribtionInterface extends JFrame {
        private JLabel LMed;
        private JComboBox CBDate;
        private String arrDate[] = { "Sat, 5:00PM 12/1/2025 ", "Mon, 5:30PM 12/3/2025" };
        private JButton CnfrmBtn, RetBtn;
        private UsersDbCommands userDb;
        private DescriptionsDbCommands descDb;

        public PrescribtionInterface(String title) {
            super(title);
            this.setSize(400, 400);
            this.setLocation(500, 400);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel P = (JPanel) this.getContentPane();
            P.setLayout(new GridLayout(3, 1));
            JPanel P1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel P2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel P3 = new JPanel();
            PName = new JLabel("Choose Patient :");
            PatPick = new JComboBox();
            LMed = new JLabel("Medication : ");
            TFPMeds = new JTextField(40);
            CnfrmBtn = new JButton("Confirm");
            Can1Btn = new JButton("Cancel");

            userDb = new UsersDbCommands();
            descDb = new DescriptionsDbCommands();
            final List<Person> doctorsPatients = userDb.getDoctorsPatients(currentDoctor.id);

            for (Person patient : doctorsPatients) {
                PatPick.addItem(patient.name);
            }

            Can1Btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }

            });

            CnfrmBtn.addActionListener(e -> {
                if(TFPMeds.getText().trim().isEmpty() )
                {
                    JOptionPane.showMessageDialog(null, "Please write a prescribtion");
                    return;
                }

                // Find the patient object that matches the selected name
                String selectedPatientName = (String) PatPick.getSelectedItem();
                Person selectedPatient = null;
                for (Person patient : doctorsPatients) {
                    if (patient.name.equals(selectedPatientName)) {
                        selectedPatient = patient;
                        break;
                    }
                }
                
                if (selectedPatient == null) {
                    JOptionPane.showMessageDialog(null, "Patient not selected or not found in your patients list");
                    return;
                }
                
                int prescriptionId = descDb.insertDescription(new Description(-1, TFPMeds.getText(), selectedPatient.id));
                JOptionPane.showMessageDialog(null, "Prescription with id " + prescriptionId +  " for patient " + selectedPatientName +
                 "Added successfully");

                


            });

            P1.add(PName);
            P1.add(PatPick);
            P2.add(LMed);
            P2.add(TFPMeds);
            P3.add(CnfrmBtn);
            P3.add(Can1Btn);
            P.add(P1);
            P.add(P2);
            P.add(P3);

            this.pack();
            this.show();

        }
    }

    public static void main(String[] args) {
        // DoctorInterface DI = new DoctorInterface("Home Page");

    }

}