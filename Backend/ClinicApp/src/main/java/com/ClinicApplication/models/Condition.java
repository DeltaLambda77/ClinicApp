package com.example.patientmatching.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conditionId;

    private String name;
    private String code;
    private String description;

    @ManyToMany(mappedBy = "conditions")
    private Set<Patient> patients = new HashSet<>();
}