package org.abiram.api.repository;

import org.abiram.api.model.Attendance;
import org.abiram.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find attendance records for a specific person
    List<Attendance> findByPerson(Person person);
    
    // Find attendance records for a person in the last 30 days
    @Query("SELECT a FROM Attendance a WHERE a.person = :person AND a.date >= :startDate")
    List<Attendance> findByPersonAndDateAfter(@Param("person") Person person, @Param("startDate") String startDate);
    
    // Find attendance records for a person in a specific month
    @Query("SELECT a FROM Attendance a WHERE a.person = :person AND a.date LIKE :monthPattern")
    List<Attendance> findByPersonAndMonth(@Param("person") Person person, @Param("monthPattern") String monthPattern);
}
