package com.example.PatientInterface;

import java.util.ArrayList;
import java.util.List;

import com.example.Data.UsersDbCommands;
import com.example.Models.Person;

public class PatientsAdder {

    public void addPatientUsers() {
        UsersDbCommands dbCommands = new UsersDbCommands();
        
        // إنشاء قائمة تحتوي على بيانات بعض المرضى
        List<Person> patientUsers = new ArrayList<>();
        // يمكن تعديل القيم وفق احتياجاتك، لاحظ أن قيمة id هنا يتم تمرير 0 أو يمكن تجاهلها إذا كانت قاعدة البيانات تولد القيمة تلقائياً
        patientUsers.add(new Person(0, "admin", "patient1@example.com","01012345678","pass", "admin", 1));
        //patientUsers.add(new Person(0, "PatientTwo", "pass2", "patient", "01087654321", "patient2@example.com", 1));
        //patientUsers.add(new Person(0, "PatientThree", "pass3", "patient", "01111222333", "patient3@example.com", 1));
        
        // إدراج كل مريض في قاعدة البيانات باستخدام الدالة InsertUser
        for (Person patient : patientUsers) {
            boolean success = dbCommands.InsertUser(patient);
            if (success) {
                System.out.println("تمت إضافة المريض: " + patient.getName());
            } else {
                System.out.println("فشل في إضافة المريض: " + patient.getName());
            }
        }
    }
    
}
