package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Leave this completely empty!
    // JpaRepository automatically gives you save(), findById(), deleteById(), etc.
    // All custom queries are now handled by ProjectMemberRepository.

}