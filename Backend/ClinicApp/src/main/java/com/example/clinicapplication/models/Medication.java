package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicationId;

    private String name;
    private String dosage;

    @ManyToMany(mappedBy = "medications")
    private Set<Patient> patients = new HashSet<>();

    @OneToMany(mappedBy = "excludedMedication")
    private Set<TrialRequirement> trialRequirements = new HashSet<>();
}
