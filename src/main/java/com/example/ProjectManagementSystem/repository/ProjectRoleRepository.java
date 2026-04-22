package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {

    Optional<ProjectRole> findByName(String name);
}