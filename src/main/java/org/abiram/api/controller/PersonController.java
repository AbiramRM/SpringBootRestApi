package org.abiram.api.controller;

import org.abiram.api.model.Attendance;
import org.abiram.api.model.AttendanceStatus;
import org.abiram.api.model.Person;
import org.abiram.api.repository.AttendanceRepository;
import org.abiram.api.repository.PersonRepository;
import org.abiram.api.service.PersonService;
import org.abiram.api.validations.PersonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/person") // Base path for this controller
@CrossOrigin(origins = "http://localhost:3000")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final AttendanceRepository attendanceRepository;
    private final PersonService personService;
    private final PersonValidate personValidate;

    public PersonController(PersonRepository personRepository, AttendanceRepository attendanceRepository, PersonService personService, PersonValidate personValidate) {
        this.personRepository = personRepository;
        this.attendanceRepository = attendanceRepository;
        this.personService = personService;
        this.personValidate = personValidate;
    }

    // POST /person
    @PostMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> addPerson(@RequestBody Map<String, Object> payload) {
        try {
            // Validate attendance status if attendance data is present
            if (payload.containsKey("attendance") && payload.get("attendance") instanceof List) {
                List<Map<String, Object>> attendanceList = (List<Map<String, Object>>) payload.get("attendance");
                ResponseEntity<String> validationResult = personValidate.validateAttendanceStatus(attendanceList);
                
                if (validationResult != null) {
                    return validationResult; // Return error if validation fails
                }
            }
            
            // Save person and attendance records
            String result = personService.savePerson(payload);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error saving person: ", e);
            return ResponseEntity.internalServerError().body("Error saving person: " + e.getMessage());
        }
    }

    // GET /person/{fname}
    @GetMapping("/{fname}")
    public ResponseEntity<?> getPersonByFirstName(@PathVariable String fname) {
        log.info("getting person information for : "+fname);
        Optional<Person> person = personRepository.findByFname(fname);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /person/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllPerson() {
        log.info("getting person information for all in Db");
        List<Person> person = personRepository.findAll();
        return ResponseEntity.ok(person);

    }

    // PUT /person/attendance/{fname}
    // Modify or add attendance records for a given person
    @PutMapping("/attendance/{fname}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> updateAttendanceByFirstName(@PathVariable String fname, @RequestBody List<Map<String, Object>> attendanceList) {
        try {
            log.info("Updating attendance for person: " + fname);
            
            // Check if person exists
            Optional<Person> personOpt = personRepository.findByFname(fname);
            if (personOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Person with name '" + fname + "' does not exist.");
            }
            
            Person person = personOpt.get();
            
            // Validate attendance statuses first
            ResponseEntity<String> validationResult = personValidate.validateAttendanceStatus(attendanceList);
            if (validationResult != null) {
                return validationResult; // Return error if validation fails
            }
            
            // Delete existing attendance records for this person
            List<Attendance> existingAttendances = attendanceRepository.findByPerson(person);
            attendanceRepository.deleteAll(existingAttendances);
            
            // Add new attendance records
            for (Map<String, Object> attendanceData : attendanceList) {
                Attendance attendance = new Attendance();
                attendance.setPerson(person);
                attendance.setName((String) attendanceData.get("name"));
                attendance.setDate((String) attendanceData.get("date"));
                
                String statusStr = (String) attendanceData.get("attendanceStatus");
                if (statusStr != null) {
                    attendance.setAttendanceStatus(AttendanceStatus.valueOf(statusStr.toUpperCase()));
                }
                
                attendanceRepository.save(attendance);
            }
            
            return ResponseEntity.ok("Attendance records updated successfully for " + fname);
            
        } catch (Exception e) {
            log.error("Error updating attendance: ", e);
            return ResponseEntity.internalServerError().body("Error updating attendance: " + e.getMessage());
        }
    }

    // GET /attendance/{fname}
    // fetch the attendance for the employee for the last 30 days
    @GetMapping("/attendance/{fname}")
    public ResponseEntity<?> getAttendanceByFirstName(@PathVariable String fname) {
        try {
            log.info("Getting attendance for person: " + fname);
            Optional<Person> personOpt = personRepository.findByFname(fname);
            
            if (personOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Person person = personOpt.get();
            List<Attendance> attendances = attendanceRepository.findByPerson(person);
            
            Map<String, Object> response = new HashMap<>();
            response.put("person", person);
            response.put("attendance", attendances);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching attendance: ", e);
            return ResponseEntity.internalServerError().body("Error fetching attendance: " + e.getMessage());
        }
    }

    // GET /attendance/{month}/{fname}
    // fetch the attendance for a given month of the above employee
    @GetMapping("/attendance/{month}/{fname}")
    public ResponseEntity<?> getAttendanceByMonthAndFirstName(@PathVariable String month, @PathVariable String fname) {
        try {
            log.info("Getting attendance for person: " + fname + " in month: " + month);
            Optional<Person> personOpt = personRepository.findByFname(fname);
            
            if (personOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Person person = personOpt.get();
            String monthPattern = month + "%"; // e.g., "2025-01%" for January 2025
            List<Attendance> attendances = attendanceRepository.findByPersonAndMonth(person, monthPattern);
            
            Map<String, Object> response = new HashMap<>();
            response.put("person", person);
            response.put("attendance", attendances);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching monthly attendance: ", e);
            return ResponseEntity.internalServerError().body("Error fetching monthly attendance: " + e.getMessage());
        }
    }

}
