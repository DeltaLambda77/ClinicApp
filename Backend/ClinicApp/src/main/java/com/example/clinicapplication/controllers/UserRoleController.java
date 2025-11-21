package com.example.clinicapplication.controllers;

import com.example.clinicapplication.dto.UserRoleDTO;
import com.example.clinicapplication.models.UserRole;
import com.example.clinicapplication.repositories.UserRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleRepository userRoleRepository;

    public UserRoleController(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        System.out.println("GET /api/user-roles called");
        List<UserRoleDTO> userRoles = userRoleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        System.out.println("Returning " + userRoles.size() + " user-role assignments");
        return ResponseEntity.ok(userRoles);
    }

    private UserRoleDTO convertToDTO(UserRole userRole) {
        return new UserRoleDTO(
                userRole.getUser().getUserId(),
                userRole.getRole().getRoleId(),
                userRole.getAssignedAt() != null ? userRole.getAssignedAt().toString() : null
        );
    }
}