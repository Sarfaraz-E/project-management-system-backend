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
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    // EXCLUSIONS ADDED HERE to break the infinite loop back to Project
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne
    private Project project;

    // EXCLUSIONS ADDED HERE to break the infinite loop with Messages
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    // EXCLUSIONS ADDED HERE to break potential loops with User
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "chat_users",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"chat_id", "users_id"})
    )
    private List<User> users = new ArrayList<>();
}