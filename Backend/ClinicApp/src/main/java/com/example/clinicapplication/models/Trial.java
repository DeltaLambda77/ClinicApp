package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Trial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TrialID")
    private Long trialId;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "MinimumAge")
    private Integer minimumAge;

    @Column(name = "MaximumAge")
    private Integer maximumAge;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @OneToMany(mappedBy = "trial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<TrialRequirement> trialRequirements = new HashSet<>();
}
