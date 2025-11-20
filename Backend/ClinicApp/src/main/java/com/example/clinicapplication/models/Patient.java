package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PatientID")
    private Long patientId;

    @Column(name = "FirstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "Sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "ContactInfo", length = 100)
    private String contactInfo;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PatientCondition> patientConditions = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PatientMedication> patientMedications = new HashSet<>();

    public enum Sex {
        M, F, Other
    }
}
