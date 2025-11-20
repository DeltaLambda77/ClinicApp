package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PatientCondition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCondition {

    @EmbeddedId
    private PatientConditionId id;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "PatientID")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @MapsId("conditionId")
    @JoinColumn(name = "ConditionID")
    private MedicalCondition condition;

    @Column(name = "DiagnosisDate", nullable = false)
    private LocalDate diagnosisDate;
}
