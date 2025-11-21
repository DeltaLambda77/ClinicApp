package com.example.clinicapplication.controllers;

import com.example.clinicapplication.dto.LoginRequest;
import com.example.clinicapplication.dto.LoginResponse;
import com.example.clinicapplication.models.User;
import com.example.clinicapplication.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")  // FIXED: Use originPatterns instead of origins
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request) {
        System.out.println("POST /api/auth/login called");
        System.out.println("Login attempt for username: " + loginRequest.getUsername());

        try {
            String ipAddress = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            LoginResponse response = authService.authenticateUser(
                    loginRequest.getUsername(),
                    loginRequest.getPassword(),
                    ipAddress,
                    userAgent
            );

            if (response.isSuccess()) {
                System.out.println("Login successful for user: " + loginRequest.getUsername());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Login failed for user: " + loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            LoginResponse errorResponse = new LoginResponse(
                    false,
                    "An error occurred during login",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        System.out.println("POST /api/auth/logout called");

        Map<String, Object> response = new HashMap<>();

        try {
            String sessionToken = extractToken(authHeader);

            if (sessionToken == null) {
                response.put("success", false);
                response.put("message", "Invalid authorization header");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean success = authService.logout(sessionToken);

            if (success) {
                response.put("success", true);
                response.put("message", "Logout successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Session not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred during logout");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateSession(@RequestHeader("Authorization") String authHeader) {
        System.out.println("GET /api/auth/validate called");

        Map<String, Object> response = new HashMap<>();

        try {
            String sessionToken = extractToken(authHeader);

            if (sessionToken == null) {
                response.put("valid", false);
                response.put("message", "Invalid authorization header");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isValid = authService.validateSession(sessionToken);

            if (isValid) {
                Optional<User> userOptional = authService.getUserBySessionToken(sessionToken);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    response.put("valid", true);
                    response.put("userId", user.getUserId());
                    response.put("username", user.getUsername());
                    response.put("firstName", user.getFirstName());
                    response.put("lastName", user.getLastName());
                    return ResponseEntity.ok(response);
                }
            }

            response.put("valid", false);
            response.put("message", "Invalid or expired session");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            System.err.println("Error during session validation: " + e.getMessage());
            e.printStackTrace();
            response.put("valid", false);
            response.put("message", "An error occurred during validation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse.UserInfo> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("GET /api/auth/me called");

        try {
            String sessionToken = extractToken(authHeader);

            if (sessionToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<User> userOptional = authService.getUserBySessionToken(sessionToken);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userOptional.get();

            var roles = user.getUserRoles().stream()
                    .map(userRole -> new LoginResponse.RoleInfo(
                            userRole.getRole().getRoleId(),
                            userRole.getRole().getRoleName(),
                            userRole.getRole().getCanAddPatients(),
                            userRole.getRole().getCanViewTrials(),
                            userRole.getRole().getCanManageTrials(),
                            userRole.getRole().getCanViewReports()
                    ))
                    .toList();

            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getUserId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    roles
            );

            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            System.err.println("Error fetching current user: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Extract session token from Authorization header
     * Expected format: "Bearer <token>"
     */
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Get client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}