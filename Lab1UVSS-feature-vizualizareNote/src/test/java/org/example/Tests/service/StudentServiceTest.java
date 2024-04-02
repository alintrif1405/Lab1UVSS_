package org.example.Tests.service;

import org.example.model.Students;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    private Students student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @BeforeEach
    void init() {
        student = new Students();
    }

    @Test
    void getAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(new Students(), new Students()));
        assertEquals(2, studentService.getAllStudents().size());
    }

    @Test
    void getById() {

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        assertEquals(Optional.of(student), studentService.getStudentById(1));
    }

    @Test
    void addStudent() {

        when(studentRepository.save(Mockito.any(Students.class))).thenReturn(student);

        Students addedStudent = studentService.addStudent(student);

        assertNotNull(addedStudent);
        assertEquals(student, addedStudent);
    }

    @Test
    void deleteStudent() {

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        boolean deleted = studentService.deleteStudent(1);

        assertTrue(deleted);
        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(1);
    }


}
