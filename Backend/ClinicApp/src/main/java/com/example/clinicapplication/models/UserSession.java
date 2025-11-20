package com.example.clinicapplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserSessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SessionID")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "SessionToken", nullable = false, unique = true, length = 255)
    private String sessionToken;

    @Column(name = "IPAddress", length = 45)
    private String ipAddress;

    @Column(name = "UserAgent", length = 255)
    private String userAgent;

    @Column(name = "LoginTime")
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(name = "LastActivityTime")
    private LocalDateTime lastActivityTime = LocalDateTime.now();

    @Column(name = "IsActive")
    private Boolean isActive = true;

    @PreUpdate
    protected void onUpdate() {
        lastActivityTime = LocalDateTime.now();
    }
}
