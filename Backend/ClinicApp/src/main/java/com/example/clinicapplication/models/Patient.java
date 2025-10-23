package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String contactInfo;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientCondition> patientConditions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "patient_medication",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private Set<Medication> medications = new HashSet<>();
}