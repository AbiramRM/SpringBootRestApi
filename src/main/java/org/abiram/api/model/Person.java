package org.abiram.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fname;
    private String lname;
    private String email;
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
