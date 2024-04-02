package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Course;
import org.example.model.CourseType;
import org.example.model.StudentCourse;
import org.example.model.Students;
import org.example.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @MockBean
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private Students student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void init() {
        student = new Students();
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void addStudent() throws Exception {
        given(studentService.addStudent(Mockito.any(Students.class))).willReturn(student);
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(student)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllStudents() throws Exception {
        List<Students> allStudents = Arrays.asList(student);

        given(studentService.getAllStudents()).willReturn(allStudents);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetCoursesForStudent() throws Exception {
        Course course = new Course();
        Set<StudentCourse> studentCourses = new HashSet<>();
        studentCourses.add(new StudentCourse());
        student.setStudentCourses(studentCourses);

        given(studentService.getStudentById(1)).willReturn(Optional.of(student));

        mockMvc.perform(get("/students/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetGrades() throws Exception {

        student.setStudentID(1);
        student.setStudentCourses(new HashSet<>());

        StudentCourse mockStudentCourse = new StudentCourse();
        mockStudentCourse.setStudentCourseID(null);
        mockStudentCourse.setStudent(student);

        Course mockCourse = new Course();
        mockCourse.setCourseID(101);
        mockCourse.setName("Math");
        mockCourse.setType(CourseType.obligatory);
        mockStudentCourse.setCourse(mockCourse);
        mockStudentCourse.setNote(8.5);

        student.getStudentCourses().add(mockStudentCourse);

        // Mocking the service method
        when(studentService.getStudentById(1)).thenReturn(java.util.Optional.of(student));

        // Performing the test
        mockMvc.perform(get("/students/1/courses/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].grade").value(8.5))
                .andExpect(jsonPath("$[0].courseName").value("Math"))
                .andExpect(jsonPath("$[0].type").value("obligatory"))
                .andExpect(jsonPath("$[0].courseId").value(101));
    }
}



