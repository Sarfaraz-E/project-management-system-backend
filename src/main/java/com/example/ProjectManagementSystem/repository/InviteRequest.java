package com.example.ProjectManagementSystem.repository;

public class InviteRequest {

    private String email;
    private Long projectId;
    private Long targetRoleId; // <-- This is the missing field

    // Default Constructor
    public InviteRequest() {
    }

    // Manual Getters and Setters (Guaranteed to compile)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTargetRoleId() {
        return targetRoleId;
    }

    public void setTargetRoleId(Long targetRoleId) {
        this.targetRoleId = targetRoleId;
    }
}