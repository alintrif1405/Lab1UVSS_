package org.example.Tests.service;

import org.example.model.Professors;
import org.example.repository.ProfessorRepository;
import org.example.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private Professors professor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @BeforeEach
    void init() {
        professor = new Professors();
    }

    @Test
    void getAllProfessors() {
        when(professorRepository.findAll()).thenReturn(List.of(new Professors(), new Professors()));
        List<Professors> professors = professorService.getAllProfessors();
        assertNotNull(professors);
        assertEquals(professors.size(), 2);
    }

    @Test
    void getProfessorById() {
        when(professorRepository.findById(1)).thenReturn(Optional.of(professor));
        assertEquals(Optional.of(professor), professorService.getProfessorById(1));
    }

    @Test
    void addProfessor() {
        when(professorRepository.save(Mockito.any(Professors.class))).thenReturn(professor);
        Professors addedProfessor = professorService.addProfessor(professor);
        assertNotNull(addedProfessor);
        assertEquals(professor, addedProfessor);
    }

    @Test
    void deleteProfessor() {

        when(professorRepository.findById(1)).thenReturn(Optional.of(new Professors()));


        boolean deleted = professorService.deleteProfessor(1);


        assertTrue(deleted);
        Mockito.verify(professorRepository, times(1)).deleteById(1);
    }


}
