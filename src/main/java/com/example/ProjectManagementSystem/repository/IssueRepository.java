package com.example.ProjectManagementSystem.repository;

import com.example.ProjectManagementSystem.model.Issues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issues,Long> {

    public List<Issues> findProjectById(Long id);
}
