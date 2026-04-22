package com.example.ProjectManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // e.g., "Project Lead", "Developer", "Viewer"
    private String name;

    // A role has many permissions, and a permission belongs to many roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();
}