package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseID;
    @Column(name = "courseName")
    private String name;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "creditNr")
    private double creditNr;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProfessorID", nullable = false)
    @JsonBackReference
    private Professors professor;
    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<StudentCourse> studentCourses;
    @Column(name = "yearOfStudy")
    private int yearOfStudy;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private CourseType type;
}
