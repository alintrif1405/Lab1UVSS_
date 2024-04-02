package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.model.Course;
import org.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Integer Id) {
        return courseRepository.findById(Id);
    }

    public Course addCourses(Course course) {
        course.setCourseID(null);
        return courseRepository.save(course);
    }

    public boolean deleteCourse(Integer Id) {
        Optional<Course> existingCourse = courseRepository.findById(Id);
        if (existingCourse.isPresent()) {
            courseRepository.deleteById(Id);
            return true;
        } else throw new EntityNotFoundException("Course with id " + Id + " has not been found");
    }


}


