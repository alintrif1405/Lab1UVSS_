package org.example.Tests.service;

import org.example.model.Course;
import org.example.repository.CourseRepository;
import org.example.service.CourseService;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void init() {
        course = new Course();
    }

    @Test
    void getAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(new Course(), new Course()));
        List<Course> courses = courseService.getAllCourses();
        assertNotNull(courses);
        assertEquals(2, courses.size());
    }

    @Test
    void getCourseById() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        assertEquals(Optional.of(course), courseService.getCourseById(1));
    }

    @Test
    void addCourses() {
        when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);
        Course addedCourse = courseService.addCourses(course);
        assertNotNull(addedCourse);
        assertEquals(course, addedCourse);
    }

    @Test
    void deleteCourse() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(new Course()));
        boolean deleted = courseService.deleteCourse(1);
        assertTrue(deleted);
        Mockito.verify(courseRepository, times(1)).deleteById(1);
    }
}
