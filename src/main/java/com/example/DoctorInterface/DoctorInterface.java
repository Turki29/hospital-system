package com.example.DoctorInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.Models.Person;

public class DoctorInterface extends JFrame {
private JButton Can1Btn, ReschdBtn,AccessBtn,CnfrmBtn,Can2Btn,LogOutBtn;
private JComboBox Apptmnts,PatPick;
private String Pat [] = { "ID : 1  |  Ali Fahad  | Age :  21  |  M  | 3:30PM 11/10/2025", "ID : 2  |  Sarah Mohammad  |  25  |  F  |  4:25PM  11/12/2025"};
private String arrPatPick [] = { "Ali Fahad ", "Sarah Mohammad "};
private JLabel LPatRec,PMeds,PName,LApptmnts;
private JTextField TFPMeds;

    public DoctorInterface(String title,Person doc)  {
    super(title);
    this.setSize(300,300);
    this.setLocation(100,400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel MainP = (JPanel) this.getContentPane();
    MainP.setLayout(new GridLayout(4,1));
    
    JPanel P1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    P1.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.GRAY));
     LPatRec = new JLabel("Patient Records :");   
     AccessBtn = new JButton("Access");
     AccessBtn.addActionListener(new AccessActionListener());
     P1.add(LPatRec);
     P1.add(AccessBtn);
     //
     JPanel P2 = new JPanel(new BorderLayout());
     JPanel P21 = new JPanel(new FlowLayout(FlowLayout.LEFT));
     P2.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.GRAY));
     LApptmnts = new JLabel("Appointments: ");
     Apptmnts = new JComboBox(Pat);
     Can1Btn = new JButton("Cancel");
     ReschdBtn = new JButton("Reschedule");
     ReschdBtn.addActionListener(new ReschdActionListener());
    P2.add(LApptmnts,BorderLayout.NORTH);
     P21.add(Apptmnts );
     P21.add(Can1Btn );
     P21.add(ReschdBtn);
     P2.add(P21,BorderLayout.CENTER);
     //
     
     JPanel P3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
     JPanel P31 = new JPanel(new GridLayout(3,2));
     JPanel P311 = new JPanel(new FlowLayout(FlowLayout.LEFT));
     GridBagConstraints a = new GridBagConstraints();
     P3.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.GRAY));
     PMeds = new JLabel("Prescribe Medication : ");
     PName = new JLabel("           Choose Patient :");
     PatPick = new JComboBox(arrPatPick);
     TFPMeds = new JTextField(30);
     CnfrmBtn = new JButton("Confirm");
     Can2Btn = new JButton("Cancel");
     
     P311.add(PMeds);
     P311.add(PName);
     P31.add(P311);
     P31.add(PatPick);
     P31.add(TFPMeds);
     P31.add(new JLabel());
     P31.add(CnfrmBtn);
     P31.add(Can2Btn);
     
     P3.add(P31);
            
    //
    
    JPanel P4 = new JPanel();
    LogOutBtn = new JButton("Log Out");
    P4.add(LogOutBtn,BorderLayout.SOUTH);
    
    
    //

     MainP.add(P1,a);
    
     MainP.add(P2,a);
     
     MainP.add(P3,a);
     
     MainP.add(P4,a);
    
    
    
    this.pack();
    this.show();
    }
    
    
    
//Listeners here 
    public class ReschdActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            new RescheduleInterface("Rescheduling");
        }
        
    }
    
    public class AccessActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e2) {
            new PatientRecordsInterface("Patient Records");
        }
        
    }
    
    
    
// 
    
    public class RescheduleInterface extends JFrame {
        private JComboBox CBDate;
        private String arrDate [] = { "Sat, 5:00PM 12/1/2025 ", "Mon, 5:30PM 12/3/2025"};
        private JButton CnfrmBtn,RetBtn;

        public RescheduleInterface(String title)  {
    super(title);
    this.setSize(400,400);
    this.setLocation(500,400);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel P = (JPanel) this.getContentPane();
    JPanel P1 = new JPanel(new FlowLayout());
    CBDate = new JComboBox(arrDate);
    CnfrmBtn = new JButton("Confirm");
    RetBtn = new JButton("Return");
    P.add(CBDate,BorderLayout.NORTH);
    P1.add(CnfrmBtn);
    P1.add(RetBtn);
    P.add(P1);
    
    
    this.pack();
    this.show();
            
        }
         }
    
      public class PatientRecordsInterface extends JFrame {
        private JList TAPats;
       private String[] arrRecord = {
            "1 - Fahad Ali",
            "2 - Sarah Mohammad",  };
        private JButton AddBtn,DelBtn;

        public PatientRecordsInterface(String title)  {
    super(title);
    this.setSize(400,400);
    this.setLocation(500,400);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel P = (JPanel) this.getContentPane();
    JPanel P1 = new JPanel(new FlowLayout());
    TAPats = new JList(arrRecord);
    AddBtn = new JButton("Add");
    DelBtn = new JButton("Delete");
    P.add(TAPats,BorderLayout.NORTH);
    P1.add(AddBtn);
    P1.add(DelBtn);
    P.add(P1);
    
    
    this.pack();
    this.show();
            
        }
        
        
        }
   
    
   
}