package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findBySessionToken(String sessionToken);
}
