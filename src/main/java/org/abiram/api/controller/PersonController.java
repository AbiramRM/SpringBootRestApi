package org.abiram.api.controller;

import org.abiram.api.model.Person;
import org.abiram.api.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person") // Base path for this controller
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // POST /person
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity.ok("Person saved to DB successfully.");
    }

    // GET /person/{fname}
    @GetMapping("/{fname}")
    public ResponseEntity<?> getPersonByFirstName(@PathVariable String fname) {
        Optional<Person> person = personRepository.findByFname(fname);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
