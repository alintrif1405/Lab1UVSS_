package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ProfessorCourseResponseDTO {
    private String firstName;
    private String lastName;
    private Set<Course> courses;
}
