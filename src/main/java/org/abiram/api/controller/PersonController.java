package org.abiram.api.controller;

import org.abiram.api.model.Person;
import org.abiram.api.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person") // Base path for this controller
@CrossOrigin(origins = "http://localhost:3000")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
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
        log.info("getting person information for : "+fname);
        Optional<Person> person = personRepository.findByFname(fname);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
