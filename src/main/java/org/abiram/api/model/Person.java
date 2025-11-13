package org.abiram.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("fname")
    private String fname;
    
    @JsonProperty("lname")
    private String lname;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Attendance> attendances = new ArrayList<>();
    
    // Constructors
    public Person() {}

    public Person(String fname, String lname, String email, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
    }
    
    // Manual setters
    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public void setLname(String lname) {
        this.lname = lname;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    // Manual getters
    public Long getId() {
        return id;
    }
    
    public String getFname() {
        return fname;
    }
    
    public String getLname() {
        return lname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
}
