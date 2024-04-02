package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Course;
import org.example.model.Professors;
import org.example.model.Students;
import org.example.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProfessorController.class)
@ExtendWith(MockitoExtension.class)
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private ProfessorController professorController;
    @MockBean
    private ProfessorService professorService;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.standaloneSetup(professorController).build();
    }

    @Test
    public void testGetAllProfessors() throws Exception {
        List<Professors> professorsList = Arrays.asList(
                new Professors(),
                new Professors()
        );

        given(professorService.getAllProfessors()).willReturn(professorsList);

        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetProfessorCourses() throws Exception {
        Professors professor = new Professors();
        Set<Course> courses = new HashSet<>();
        courses.add(new Course());
        professor.setCourses(courses);

        given(professorService.getProfessorById(1)).willReturn(Optional.of(professor));

        mockMvc.perform(get("/professors/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses", hasSize(1)));

    }

    @Test
    public void testAddProfessor() throws Exception {
        Professors professorToAdd = new Professors();

        given(professorService.addProfessor(any(Professors.class))).willReturn(professorToAdd);

        mockMvc.perform(post("/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(professorToAdd)))
                .andExpect(status().isCreated());

    }

}
