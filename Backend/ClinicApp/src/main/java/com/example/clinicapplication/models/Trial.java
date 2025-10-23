package com.example.clinicapplication.models;

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
public class Trial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trialId;

    private String title;
    private Integer minimumAge;
    private Integer maximumAge;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "trial", cascade = CascadeType.ALL)
    private Set<TrialRequirement> trialRequirements = new HashSet<>();
}