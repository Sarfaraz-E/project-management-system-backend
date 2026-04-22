package com.example.ProjectManagementSystem.security;

import com.example.ProjectManagementSystem.model.Permission;
import com.example.ProjectManagementSystem.model.ProjectMember;
import com.example.ProjectManagementSystem.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("projectSecurity")
public class ProjectSecurityEvaluator {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public boolean hasPermission(Authentication authentication, Long projectId, String requiredPermission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Get the logged-in user's email from the JWT token authentication
        String userEmail = authentication.getName();

        // Look up their role in THIS specific project
        Optional<ProjectMember> memberOpt = projectMemberRepository.findByUserEmailAndProjectId(userEmail, projectId);

        if (memberOpt.isEmpty()) {
            return false; // They aren't in the project at all
        }

        // Check if their assigned role contains the permission we are asking for
        return memberOpt.get().getRole().getPermissions().stream()
                .map(Permission::getName)
                .anyMatch(permissionName -> permissionName.equals(requiredPermission));
    }
}