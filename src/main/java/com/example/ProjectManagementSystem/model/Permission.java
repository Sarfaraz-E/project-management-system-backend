package com.example.ProjectManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // e.g., "CREATE_ISSUE", "UPDATE_PROJECT", "DELETE_COMMENT"
    @Column(unique = true)
    private String name;
}