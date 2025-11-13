package org.abiram.api.service;

import org.abiram.api.model.Attendance;
import org.abiram.api.model.AttendanceStatus;
import org.abiram.api.model.Person;
import org.abiram.api.repository.AttendanceRepository;
import org.abiram.api.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final AttendanceRepository attendanceRepository;

    public PersonService(PersonRepository personRepository, AttendanceRepository attendanceRepository) {
        this.personRepository = personRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @SuppressWarnings("unchecked")
    public String savePerson(Map<String, Object> payload) throws Exception {
        // Extract person details
        Person person = new Person();
        person.setFname((String) payload.get("fname"));
        person.setLname((String) payload.get("lname"));
        person.setEmail((String) payload.get("email"));
        person.setPhone((String) payload.get("phone"));
        
        // Save person first
        Person savedPerson = personRepository.save(person);
        
        // Save attendance records if present
        if (payload.containsKey("attendance") && payload.get("attendance") instanceof List) {
            List<Map<String, Object>> attendanceList = (List<Map<String, Object>>) payload.get("attendance");
            
            for (Map<String, Object> attendanceData : attendanceList) {
                Attendance attendance = new Attendance();
                attendance.setPerson(savedPerson);
                attendance.setName((String) attendanceData.get("name"));
                attendance.setDate((String) attendanceData.get("date"));
                
                String statusStr = (String) attendanceData.get("attendanceStatus");
                if (statusStr != null) {
                    attendance.setAttendanceStatus(AttendanceStatus.valueOf(statusStr.toUpperCase()));
                }
                
                attendanceRepository.save(attendance);
            }
            
            return "Person and attendance saved to DB successfully.";
        }
        
        return "Person saved to DB successfully.";
    }
}

