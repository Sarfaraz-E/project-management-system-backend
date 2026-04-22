package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.config.request.IssueRequest;
import com.example.ProjectManagementSystem.model.Issues;
import com.example.ProjectManagementSystem.model.User;

import java.util.List;
import java.util.Optional;

public interface IssueService {
    Issues getIssueById(Long issueId) throws Exception;

    List<Issues> getIssuesByProjectId(Long projectId)throws Exception;

    Issues createIssue(IssueRequest issue, User user)throws Exception;

    Issues createIssue(IssueRequest issueRequest, Long projectId, User user) throws Exception;

    void deleteIssue(Long issueId, Long userId)throws Exception;

    Issues addUserToIssue(Long issueId,Long userId)throws Exception;

    Issues updateStatus(Long issueId,String status)throws Exception;
}
