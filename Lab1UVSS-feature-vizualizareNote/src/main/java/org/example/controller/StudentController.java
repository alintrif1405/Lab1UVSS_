package org.example.controller;


import jakarta.persistence.EntityNotFoundException;
import org.example.model.Course;
import org.example.model.StudentCourse;
import org.example.model.StudentGradesDTO;
import org.example.model.Students;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Students> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getStudentById(@PathVariable Integer id) {
        Students student = studentService.getStudentById(id).orElse(null);
        return (student != null) ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<Set<StudentCourse>> getCoursesForStudent(@PathVariable Integer id) {
        Students student = studentService.getStudentById(id).orElse(null);

        if (student != null) {
            Set<StudentCourse> studentCourses = student.getStudentCourses();

            if (studentCourses != null) {
                return ResponseEntity.ok(studentCourses);
            } else {
                return ResponseEntity.ok(Collections.emptySet());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Students student) {
        {
            Students addedStudent = studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedStudent);

        }
    }

    @GetMapping("/{id}/courses/grades")
    public ResponseEntity<Set<StudentGradesDTO>> getGrades(@PathVariable Integer id) {
        Students student = studentService.getStudentById(id).orElse(null);
        Set<StudentGradesDTO> studentGradesDTO = new HashSet<>();

        if (student != null && student.getStudentCourses() != null) {
            for (StudentCourse studentCourse : student.getStudentCourses()) {

                StudentGradesDTO gradesDTO = new StudentGradesDTO();

                // Set grade information
                gradesDTO.setGrade(studentCourse.getNote());

                // Set course information
                Course course = studentCourse.getCourse();
                if (course != null) {
                    gradesDTO.setCourseName(course.getName());
                    gradesDTO.setType(course.getType());
                    gradesDTO.setCourseId(course.getCourseID());
                }

                studentGradesDTO.add(gradesDTO);
            }
            return ResponseEntity.ok(studentGradesDTO);


        } else return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
