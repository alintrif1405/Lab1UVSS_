package org.example.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.model.Students;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;


    public List<Students> getAllStudents() {
        return studentRepository.findAll();

    }

    public Optional<Students> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public Students addStudent(Students student) {
        student.setStudentID(null);
        return studentRepository.save(student);
    }


    public boolean deleteStudent(Integer id) {
        Optional<Students> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            studentRepository.deleteById(id);

            return true;
        } else {
            throw new EntityNotFoundException("Student with ID " + id + " not found");
        }
    }


}
