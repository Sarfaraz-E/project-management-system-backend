package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.Comment;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByIssuesId(Long issueId);
}
