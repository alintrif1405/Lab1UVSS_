package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class StudentGradesDTO {
    private double grade;
    private String courseName;
    private CourseType type;
    private Integer courseId;
}
