package org.example.Key;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.model.Course;
import org.example.model.Students;

import java.io.Serializable;
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StudentCourseKey implements Serializable {

    @Column(name="studentFK")
    private Integer studentID;
    @Column(name="courseFK")
    private Integer courseID;
}
