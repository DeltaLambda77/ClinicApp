package com.example.clinicapplication.dto;

import java.util.List;

public class LoginResponse {
    private boolean success;
    private String message;
    private String sessionToken;
    private UserInfo user;

    public LoginResponse() {}

    public LoginResponse(boolean success, String message, String sessionToken, UserInfo user) {
        this.success = success;
        this.message = message;
        this.sessionToken = sessionToken;
        this.user = user;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    // Inner class for user information
    public static class UserInfo {
        private Long userId;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private List<RoleInfo> roles;

        public UserInfo() {}

        public UserInfo(Long userId, String username, String firstName, String lastName,
                        String email, List<RoleInfo> roles) {
            this.userId = userId;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.roles = roles;
        }

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<RoleInfo> getRoles() {
            return roles;
        }

        public void setRoles(List<RoleInfo> roles) {
            this.roles = roles;
        }
    }

    // Inner class for role information
    public static class RoleInfo {
        private Long roleId;
        private String roleName;
        private Boolean canAddPatients;
        private Boolean canViewTrials;
        private Boolean canManageTrials;
        private Boolean canViewReports;

        public RoleInfo() {}

        public RoleInfo(Long roleId, String roleName, Boolean canAddPatients,
                        Boolean canViewTrials, Boolean canManageTrials, Boolean canViewReports) {
            this.roleId = roleId;
            this.roleName = roleName;
            this.canAddPatients = canAddPatients;
            this.canViewTrials = canViewTrials;
            this.canManageTrials = canManageTrials;
            this.canViewReports = canViewReports;
        }

        // Getters and Setters
        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public Boolean getCanAddPatients() {
            return canAddPatients;
        }

        public void setCanAddPatients(Boolean canAddPatients) {
            this.canAddPatients = canAddPatients;
        }

        public Boolean getCanViewTrials() {
            return canViewTrials;
        }

        public void setCanViewTrials(Boolean canViewTrials) {
            this.canViewTrials = canViewTrials;
        }

        public Boolean getCanManageTrials() {
            return canManageTrials;
        }

        public void setCanManageTrials(Boolean canManageTrials) {
            this.canManageTrials = canManageTrials;
        }

        public Boolean getCanViewReports() {
            return canViewReports;
        }

        public void setCanViewReports(Boolean canViewReports) {
            this.canViewReports = canViewReports;
        }
    }
}