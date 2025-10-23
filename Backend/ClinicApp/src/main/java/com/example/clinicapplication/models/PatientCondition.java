package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient_condition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCondition {

    @EmbeddedId
    private PatientConditionId id;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @MapsId("conditionId")
    @JoinColumn(name = "condition_id")
    private Condition condition;

    private LocalDate diagnosisDate;
}
