package com.example.clinicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Long roleId;

    @Column(name = "RoleName", nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CanAddPatients")
    private Boolean canAddPatients = false;

    @Column(name = "CanViewTrials")
    private Boolean canViewTrials = false;

    @Column(name = "CanManageTrials")
    private Boolean canManageTrials = false;

    @Column(name = "CanViewReports")
    private Boolean canViewReports = false;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();
}
