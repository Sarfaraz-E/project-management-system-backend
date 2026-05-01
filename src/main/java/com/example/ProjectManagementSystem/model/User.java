package com.example.ProjectManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Not show password to user
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Issues> assignedIssues = new ArrayList<>();

    private int projectSize;

    // NEW: Links the user to their roles in various projects
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMemberships = new ArrayList<>();
}