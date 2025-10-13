package org.abiram.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
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

    // Constructors
    public Person() {}

    public Person(String fname, String lname, String email, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
    }
}
