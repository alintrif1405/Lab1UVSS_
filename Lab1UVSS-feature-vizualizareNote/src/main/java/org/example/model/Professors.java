package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Professors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer professorID;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "firstname")
    private String firstname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 15)
    private ERole role;


    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Course> courses;

}