package com.example.ProjectManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}