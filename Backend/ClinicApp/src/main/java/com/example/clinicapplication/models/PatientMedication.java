package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_medication")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String medicationName;
    private String dosage;
    private String frequency;
}
