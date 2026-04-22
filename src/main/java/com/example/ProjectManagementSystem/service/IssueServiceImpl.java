package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.config.request.IssueRequest;
import com.example.ProjectManagementSystem.model.Issues;
import com.example.ProjectManagementSystem.model.Project;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Override
    public Issues getIssueById(Long issueId) throws Exception {
        Optional<Issues> issues = issueRepository.findById(issueId);
        if (issues.isPresent()){
            return issues.get();
        }
        throw new Exception("No issues found with issueId" + issueId);
    }

    @Override
    public List<Issues> getIssuesByProjectId(Long projectId) throws Exception {
        return issueRepository.findProjectById(projectId);
    }

    @Override
    public Issues createIssue(IssueRequest issue, User user) throws Exception {
        return null;
    }

    @Override
    public Issues createIssue(IssueRequest issueRequest, Long projectId, User user) throws Exception {
        // This will now throw a clear error if the project doesn't exist,
        // rather than a "null" pointer crash.
        Project project = projectService.getProjectById(projectId);

        Issues issues = new Issues();
        issues.setTitle(issueRequest.getTitle());
        issues.setDescription(issueRequest.getDescription());
        issues.setStatus(issueRequest.getStatus());
        issues.setProjectId(projectId); // Use the URL ID
        issues.setProject(project);
        issues.setPriority(issueRequest.getPriority());
        issues.setDueDate(issueRequest.getDueDate());

        return issueRepository.save(issues);
    }
    @Override
    public void deleteIssue(Long issueId, Long userId) throws Exception {
       getIssueById(issueId);
       issueRepository.deleteById(issueId);
    }

    @Override
    public Issues addUserToIssue(Long issueId, Long userId) throws Exception {
      User user = userService.findUserById(userId);
      Issues issues = getIssueById(issueId);
      issues.setAssignee(user);

      return issueRepository.save(issues);
    }

    @Override
    public Issues updateStatus(Long issueId, String status) throws Exception {
        Issues issues = getIssueById(issueId);
        issues.setStatus(status);

        return issueRepository.save(issues);

    }
}
