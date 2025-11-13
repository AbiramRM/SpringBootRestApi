package org.abiram.api.model;

import jakarta.persistence.*;

@Entity
@Table(name="attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persons_id", nullable = false)
    private Person person;
    private String name;
    private String date;
    
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    // Manual setters
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    
    // Manual getters
    public Long getId() {
        return id;
    }
    
    public Person getPerson() {
        return person;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDate() {
        return date;
    }
    
    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }
}
