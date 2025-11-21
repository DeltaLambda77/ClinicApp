package com.example.clinicapplication.dto;

public class UserRoleDTO {
    private Long userId;
    private Long roleId;
    private String assignedAt;

    public UserRoleDTO() {}

    public UserRoleDTO(Long userId, Long roleId, String assignedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public String getAssignedAt() { return assignedAt; }
    public void setAssignedAt(String assignedAt) { this.assignedAt = assignedAt; }
}