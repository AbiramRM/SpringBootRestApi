package org.abiram.api.repository;

import org.abiram.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByFname(String fname);

    @Override
    List<Person> findAll();
}
