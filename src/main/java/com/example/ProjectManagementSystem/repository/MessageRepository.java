package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByChatIdOrderByCreatedAtAsc(Long id);
}
