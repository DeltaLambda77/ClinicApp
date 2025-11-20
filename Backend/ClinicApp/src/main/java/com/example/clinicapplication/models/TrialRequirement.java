package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TrialRequirement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrialRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequirementID")
    private Long requirementId;

    @ManyToOne
    @JoinColumn(name = "TrialID", nullable = false)
    private Trial trial;

    @ManyToOne
    @JoinColumn(name = "ConditionID")
    private MedicalCondition condition;

    @ManyToOne
    @JoinColumn(name = "ExcludedMedicationID")
    private Medication excludedMedication;

    @Column(name = "Notes", columnDefinition = "TEXT")
    private String notes;
}
