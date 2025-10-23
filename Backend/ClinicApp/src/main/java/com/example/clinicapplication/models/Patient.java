package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public String getSex() { return sex; }

    public String getContactInfo() { return contactInfo; }

    public Long getPatientId() { return patientId; }


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PatientCondition> patientConditions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "patient_medication",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    @JsonIgnore
    private Set<Medication> medications = new HashSet<>();
}