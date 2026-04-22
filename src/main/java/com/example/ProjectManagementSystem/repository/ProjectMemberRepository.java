package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    // THIS IS THE MOST IMPORTANT METHOD FOR YOUR SECURITY EVALUATOR
    // It finds the exact role a specific user has in a specific project
    Optional<ProjectMember> findByUserEmailAndProjectId(String email, Long projectId);

    // Alternative version if you prefer looking up by User ID instead of Email
    Optional<ProjectMember> findByUserIdAndProjectId(Long userId, Long projectId);

    // Useful for displaying a "Team Directory" on the project dashboard
    List<ProjectMember> findByProjectId(Long projectId);

    // Useful for showing the user all the projects they are a part of on their home screen
    List<ProjectMember> findByUserId(Long userId);
}