package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MedicalCondition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConditionID")
    private Long conditionId;

    @Column(name = "Name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "Code", unique = true, length = 20)
    private String code;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "condition", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PatientCondition> patientConditions = new HashSet<>();

    @OneToMany(mappedBy = "condition")
    @JsonIgnore
    private Set<TrialRequirement> trialRequirements = new HashSet<>();
}
