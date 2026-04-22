package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Useful for checking if a permission already exists before inserting
    Optional<Permission> findByName(String name);
}