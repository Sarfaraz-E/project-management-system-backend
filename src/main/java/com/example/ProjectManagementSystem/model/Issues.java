package com.example.ProjectManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Issues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    // FIX: Tells Hibernate to only use this for reading, preventing the duplicate mapping crash
    @Column(name = "project_id", insertable = false, updatable = false)
    private Long projectId;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags= new ArrayList<>();

    @ManyToOne
    private User assignee;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id") // FIX: Explicitly tells Hibernate this manages the project_id column
    private Project project;

    @JsonIgnore
    @OneToMany(mappedBy = "issues",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
