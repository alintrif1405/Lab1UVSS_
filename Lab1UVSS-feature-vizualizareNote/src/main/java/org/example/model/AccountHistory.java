package org.example.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_histories")
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="old_lastname")
    private String oldLastname;

    @Column(name="old_firstname")
    private String oldFirstname;

    @Column(name="old_email")
    private String oldEmail;

    @Column(name="old_password")
    private String oldPassword;

    @Enumerated(EnumType.STRING)
    @Column(name="old_role", length = 20)
    private ERole oldRole;

    @Column(name="new_lastname")
    private String newLastname;

    @Column(name="new_firstname")
    private String newFirstname;

    @Column(name="new_email")
    private String newEmail;

    @Column(name="new_password")
    private String newPassword;

    @Enumerated(EnumType.STRING)
    @Column(name="new_role", length = 20)
    private ERole newRole;

    @Column(name="date_modified")
    private LocalDate dateModified;

}
