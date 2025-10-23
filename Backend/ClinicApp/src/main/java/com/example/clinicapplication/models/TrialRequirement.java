package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrialRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementId;

    @ManyToOne
    @JoinColumn(name = "trial_id")
    private Trial trial;

    @ManyToOne(optional = true)
    @JoinColumn(name = "condition_id")
    private Condition condition;

    @ManyToOne(optional = true)
    @JoinColumn(name = "excluded_medication_id")
    private Medication excludedMedication;

    private String notes;
}
