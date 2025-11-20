package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserRoles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "RoleID")
    private Role role;

    @Column(name = "AssignedAt")
    private LocalDateTime assignedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "AssignedBy")
    private User assignedBy;
}
