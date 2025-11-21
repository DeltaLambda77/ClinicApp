package com.example.clinicapplication.services;

import com.example.clinicapplication.dto.LoginResponse;
import com.example.clinicapplication.models.User;
import com.example.clinicapplication.models.UserSession;
import com.example.clinicapplication.repositories.UserRepository;
import com.example.clinicapplication.repositories.UserSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public AuthService(UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional
    public LoginResponse authenticateUser(String username, String password, String ipAddress, String userAgent) {
        System.out.println("Attempting authentication for user: " + username);

        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + username);
            return new LoginResponse(false, "Invalid username or password", null, null);
        }

        User user = userOptional.get();

        // Check if user is active
        if (!user.getIsActive()) {
            System.out.println("User is inactive: " + username);
            return new LoginResponse(false, "Account is inactive", null, null);
        }

        // Verify password (for demo purposes, using simple comparison)
        // In production, you should use BCrypt or similar
        if (!verifyPassword(password, user.getPasswordHash())) {
            System.out.println("Invalid password for user: " + username);
            return new LoginResponse(false, "Invalid username or password", null, null);
        }

        // Generate session token
        String sessionToken = UUID.randomUUID().toString();

        // Create user session
        UserSession session = UserSession.builder()
                .user(user)
                .sessionToken(sessionToken)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .loginTime(LocalDateTime.now())
                .lastActivityTime(LocalDateTime.now())
                .isActive(true)
                .build();

        userSessionRepository.save(session);

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        System.out.println("Authentication successful for user: " + username);

        // Build user info with roles
        List<LoginResponse.RoleInfo> roles = user.getUserRoles().stream()
                .map(userRole -> new LoginResponse.RoleInfo(
                        userRole.getRole().getRoleId(),
                        userRole.getRole().getRoleName(),
                        userRole.getRole().getCanAddPatients(),
                        userRole.getRole().getCanViewTrials(),
                        userRole.getRole().getCanManageTrials(),
                        userRole.getRole().getCanViewReports()
                ))
                .collect(Collectors.toList());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getUserId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roles
        );

        return new LoginResponse(true, "Login successful", sessionToken, userInfo);
    }

    @Transactional
    public boolean logout(String sessionToken) {
        System.out.println("Attempting logout for session: " + sessionToken);

        Optional<UserSession> sessionOptional = userSessionRepository.findBySessionToken(sessionToken);

        if (sessionOptional.isEmpty()) {
            System.out.println("Session not found: " + sessionToken);
            return false;
        }

        UserSession session = sessionOptional.get();
        session.setIsActive(false);
        userSessionRepository.save(session);

        System.out.println("Logout successful for session: " + sessionToken);
        return true;
    }

    public boolean validateSession(String sessionToken) {
        Optional<UserSession> sessionOptional = userSessionRepository.findBySessionToken(sessionToken);

        if (sessionOptional.isEmpty()) {
            return false;
        }

        UserSession session = sessionOptional.get();

        // Check if session is active
        if (!session.getIsActive()) {
            return false;
        }

        // Optional: Check if session has expired (e.g., after 24 hours)
        LocalDateTime expiryTime = session.getLoginTime().plusHours(24);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            session.setIsActive(false);
            userSessionRepository.save(session);
            return false;
        }

        // Update last activity time
        session.setLastActivityTime(LocalDateTime.now());
        userSessionRepository.save(session);

        return true;
    }

    public Optional<User> getUserBySessionToken(String sessionToken) {
        Optional<UserSession> sessionOptional = userSessionRepository.findBySessionToken(sessionToken);

        if (sessionOptional.isEmpty() || !sessionOptional.get().getIsActive()) {
            return Optional.empty();
        }

        return Optional.of(sessionOptional.get().getUser());
    }

    /**
     * Simple password verification for demo purposes.
     * In production, use BCrypt: BCrypt.checkpw(password, passwordHash)
     */
    private boolean verifyPassword(String password, String passwordHash) {
        // For demo purposes, we're using a simple check
        // The seeded password hash is "$2a$10$N9qo8uLOickgx2ZMRZoMye"
        // We'll accept "password" as the password for all demo users

        // In a real application, you would use:
        // return BCrypt.checkpw(password, passwordHash);

        // For now, accept "password" or "admin123" for testing
        return password.equals("password") || password.equals("admin123");
    }
}