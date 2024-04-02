package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Course;
import org.example.model.Professors;
import org.example.model.Students;
import org.example.service.CourseService;
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



@WebMvcTest(CourseController.class)
@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CourseController courseController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }
    @Test
    public void testGetAllCourses() throws Exception {
        List<Course> coursesList = Arrays.asList(
                new Course(),
                new Course()
        );

        given(courseService.getAllCourses()).willReturn(coursesList);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetCourseById() throws Exception {
        Course course = new Course();

        given(courseService.getCourseById(1)).willReturn(Optional.of(course));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk());

    }

    @Test
    public void testAddCourse() throws Exception {
        Course courseToAdd = new Course();

        given(courseService.addCourses(any(Course.class))).willReturn(courseToAdd);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseToAdd)))
                .andExpect(status().isCreated());

    }


}


