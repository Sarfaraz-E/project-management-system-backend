package com.example.ProjectManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g., "Project Lead", "Developer", "Viewer"
    private String name;

    // A role has many permissions, and a permission belongs to many roles

    @ManyToMany
    @JoinTable(            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>(); // <-- Add the initialization here
}


