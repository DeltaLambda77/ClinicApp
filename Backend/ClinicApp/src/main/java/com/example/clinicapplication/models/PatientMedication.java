package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PatientMedication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMedication {

    @EmbeddedId
    private PatientMedicationId id;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "PatientID")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @MapsId("medicationId")
    @JoinColumn(name = "MedicationID")
    private Medication medication;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;
}
