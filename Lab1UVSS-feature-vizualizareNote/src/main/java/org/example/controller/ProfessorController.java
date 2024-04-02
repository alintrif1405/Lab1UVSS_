package org.example.controller;


import jakarta.persistence.EntityNotFoundException;
import org.example.model.Course;
import org.example.model.ProfessorCourseResponseDTO;
import org.example.model.Professors;
import org.example.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/professors")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public List<Professors> getAllProfessors() {
        return professorService.getAllProfessors();
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<ProfessorCourseResponseDTO> getProfessorCourses(@PathVariable Integer id) {
        Professors professor = professorService.getProfessorById(id).orElse(null);
        if (professor != null) {
            Set<Course> courses = professor.getCourses();
            String professorFirstName = professor.getFirstname();
            String professorLastName = professor.getLastname();

            ProfessorCourseResponseDTO response = new ProfessorCourseResponseDTO();

            response.setLastName(professorLastName);
            response.setFirstName(professorFirstName);
            response.setCourses(courses != null ? courses : Collections.emptySet());


            return ResponseEntity.ok(response);


        } else return ResponseEntity.notFound().build();

    }


    @GetMapping("/{id}")
    public ResponseEntity<Professors> getProfessorById(@PathVariable Integer id) {
        Professors professor = professorService.getProfessorById(id).orElse(null);
        return (professor != null) ? ResponseEntity.ok(professor) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> addProfessor(@RequestBody Professors professor) {
        {
            Professors addedProfessor = professorService.addProfessor(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProfessor);

        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Integer id) {
        try {
            professorService.deleteProfessor(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
