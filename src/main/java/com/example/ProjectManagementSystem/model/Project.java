package com.example.ProjectManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String category;

    private List<String> tag = new ArrayList<>();

    // EXCLUSIONS ADDED HERE to break the infinite loop with Chat
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;

    @ManyToOne
    private User owner;

    // EXCLUSIONS ADDED HERE to break the infinite loop with Issues
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issues> issues = new ArrayList<>();

    // EXCLUSIONS ADDED HERE to break the infinite loop with ProjectMembers
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();
}