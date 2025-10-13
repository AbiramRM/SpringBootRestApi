package org.abiram.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abiram.api.model.Person;
import org.abiram.api.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonController personController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        objectMapper = new ObjectMapper();
    }

    // Test cases for addPerson method
    @Test
    void testAddPerson_Success() throws Exception {
        // Given
        Person person = new Person("John", "Doe", "john.doe@example.com", "1234567890");
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person saved to DB successfully."));

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testAddPerson_WithNullFields() throws Exception {
        // Given
        Person person = new Person("John", null, null, null);

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person saved to DB successfully."));

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testAddPerson_WithEmptyJson() throws Exception {
        // Given
        Person person = new Person();

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person saved to DB successfully."));

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testAddPerson_InvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    // Test cases for getPersonByFirstName method
    @Test
    void testGetPersonByFirstName_Success() throws Exception {
        // Given
        String firstName = "John";
        Person person = new Person("John", "Doe", "john.doe@example.com", "1234567890");
        when(personRepository.findByFname(firstName)).thenReturn(Optional.of(person));

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fname").value("John"))
                .andExpect(jsonPath("$.lname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"));

        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testGetPersonByFirstName_NotFound() throws Exception {
        // Given
        String firstName = "NonExistent";
        when(personRepository.findByFname(firstName)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isNotFound());

        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testGetPersonByFirstName_WithSpecialCharacters() throws Exception {
        // Given
        String firstName = "José";
        Person person = new Person("José", "García", "jose.garcia@example.com", "1234567890");
        when(personRepository.findByFname(firstName)).thenReturn(Optional.of(person));

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("José"))
                .andExpect(jsonPath("$.lname").value("García"));

        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testGetPersonByFirstName_WithSpaces() throws Exception {
        // Given
        String firstName = "Mary Jane";
        Person person = new Person("Mary Jane", "Watson", "mary.jane@example.com", "1234567890");
        when(personRepository.findByFname(firstName)).thenReturn(Optional.of(person));

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Mary Jane"));

        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testGetPersonByFirstName_EmptyString() throws Exception {
        // Given
        String firstName = "nonexistent";
        when(personRepository.findByFname(firstName)).thenReturn(Optional.empty());

        // When & Then - Test with a non-existent name that should return 404
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isNotFound());

        verify(personRepository, times(1)).findByFname(firstName);
    }

    // Unit tests for controller methods (without MockMvc)
    @Test
    void testAddPerson_UnitTest() {
        // Given
        Person person = new Person("Jane", "Smith", "jane.smith@example.com", "9876543210");
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // When
        ResponseEntity<String> response = personController.addPerson(person);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Person saved to DB successfully.", response.getBody());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testGetPersonByFirstName_UnitTest_Success() {
        // Given
        String firstName = "Alice";
        Person person = new Person("Alice", "Johnson", "alice.johnson@example.com", "5555555555");
        when(personRepository.findByFname(firstName)).thenReturn(Optional.of(person));

        // When
        ResponseEntity<?> response = personController.getPersonByFirstName(firstName);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(person, response.getBody());
        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testGetPersonByFirstName_UnitTest_NotFound() {
        // Given
        String firstName = "Bob";
        when(personRepository.findByFname(firstName)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = personController.getPersonByFirstName(firstName);

        // Then
        assertEquals(404, response.getStatusCode().value());
        verify(personRepository, times(1)).findByFname(firstName);
    }

    // Edge case tests
    @Test
    void testGetPersonByFirstName_CaseSensitive() throws Exception {
        // Given
        String firstName = "john";
        Person person = new Person("John", "Doe", "john.doe@example.com", "1234567890");
        when(personRepository.findByFname(firstName)).thenReturn(Optional.of(person));

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("John"));

        verify(personRepository, times(1)).findByFname(firstName);
    }

    @Test
    void testAddPerson_WithVeryLongFields() throws Exception {
        // Given
        String longString = "a".repeat(1000);
        Person person = new Person(longString, longString, longString + "@example.com", longString);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person saved to DB successfully."));

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testGetPersonByFirstName_WithNumbers() throws Exception {
        // Given
        String firstName = "123";
        when(personRepository.findByFname(firstName)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/person/{fname}", firstName))
                .andExpect(status().isNotFound());

        verify(personRepository, times(1)).findByFname(firstName);
    }
}
