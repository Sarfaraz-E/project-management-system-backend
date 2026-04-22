package com.example.ProjectManagementSystem.config;

import com.example.ProjectManagementSystem.model.Permission;
import com.example.ProjectManagementSystem.model.ProjectRole;
import com.example.ProjectManagementSystem.repository.PermissionRepository;
import com.example.ProjectManagementSystem.repository.ProjectRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ProjectRoleRepository projectRoleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("--- Checking and Seeding Database Roles & Permissions ---");

        // 1. Define all possible permissions in the system
        List<String> permissionNames = Arrays.asList(
                "READ_PROJECT",
                "UPDATE_PROJECT",
                "DELETE_PROJECT",
                "INVITE_USERS",
                "REMOVE_USERS",
                "CREATE_ISSUE",
                "UPDATE_ISSUE",
                "DELETE_ISSUE",
                "CREATE_COMMENT",
                "DELETE_COMMENT"
        );

        // 2. Save permissions to the database if they don't exist
        for (String permName : permissionNames) {
            if (permissionRepository.findByName(permName).isEmpty()) {
                Permission permission = new Permission();
                permission.setName(permName);
                permissionRepository.save(permission);
            }
        }

        // 3. Create the "PROJECT_OWNER" Role (Has all permissions)
        createRoleIfNotExists("PROJECT_OWNER", permissionNames);

        // 4. Create an "ADMIN" Role (Cannot delete the project, but can manage users and issues)
        createRoleIfNotExists("ADMIN", Arrays.asList(
                "READ_PROJECT", "UPDATE_PROJECT", "INVITE_USERS", "REMOVE_USERS",
                "CREATE_ISSUE", "UPDATE_ISSUE", "DELETE_ISSUE",
                "CREATE_COMMENT", "DELETE_COMMENT"
        ));

        // 5. Create a "DEVELOPER" Role (Can work on issues and comment, cannot manage settings/users)
        createRoleIfNotExists("DEVELOPER", Arrays.asList(
                "READ_PROJECT", "CREATE_ISSUE", "UPDATE_ISSUE", "CREATE_COMMENT"
        ));

        // 6. Create a "VIEWER" Role (Read-only access)
        createRoleIfNotExists("VIEWER", Arrays.asList(
                "READ_PROJECT"
        ));

        System.out.println("--- Database Seeding Complete ---");
    }

    private void createRoleIfNotExists(String roleName, List<String> permissions) {
        Optional<ProjectRole> existingRole = projectRoleRepository.findByName(roleName);

        if (existingRole.isEmpty()) {
            ProjectRole role = new ProjectRole();
            role.setName(roleName);

            // Fetch the actual Permission objects from the DB and assign them
            for (String permName : permissions) {
                permissionRepository.findByName(permName).ifPresent(permission -> {
                    role.getPermissions().add(permission);
                });
            }

            projectRoleRepository.save(role);
        }
    }
}