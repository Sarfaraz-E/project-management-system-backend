package com.example.ProjectManagementSystem.controller;

import com.example.ProjectManagementSystem.config.request.IssueRequest;
import com.example.ProjectManagementSystem.model.IssueDTO;
import com.example.ProjectManagementSystem.model.Issues;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.response.MessageResponse;
import com.example.ProjectManagementSystem.service.IssueService;
import com.example.ProjectManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@Tag(name = "04 - Issue Tracking", description = "Issue tracking APIs")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issues> getIssueById(@PathVariable Long issueId) throws Exception {
        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issues>> getIssueByProjectId(@PathVariable Long projectId)
            throws Exception {
        return ResponseEntity.ok(issueService.getIssuesByProjectId(projectId));
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<IssueDTO> createIssue(
            @RequestBody IssueRequest issue,
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String token) throws Exception {

        User tokenUser = userService.findUserProfileByJwt(token);

        // Service now handles the mapping of Issue to Project using the ID from the URL
        Issues createdIssue = issueService.createIssue(issue, projectId, tokenUser);

        // Manual Mapping from Entity to DTO
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setId(createdIssue.getId());
        issueDTO.setTitle(createdIssue.getTitle());
        issueDTO.setDescription(createdIssue.getDescription());
        issueDTO.setStatus(createdIssue.getStatus());
        issueDTO.setPriority(createdIssue.getPriority());
        issueDTO.setDueDate(createdIssue.getDueDate());
        issueDTO.setTags(createdIssue.getTags());
        issueDTO.setProject(createdIssue.getProject());
        issueDTO.setProjectID(projectId);
        issueDTO.setAssignee(createdIssue.getAssignee());

        return ResponseEntity.ok(issueDTO);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable Long issueId,
                                                       @RequestHeader("Authorization") String token)
            throws Exception {
        User user = userService.findUserProfileByJwt(token);
        issueService.deleteIssue(issueId, user.getId());

        MessageResponse res = new MessageResponse();
        res.setMessage("Issue deleted successfully");

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issues> addUserToIssue(@PathVariable Long issueId,
                                                 @PathVariable Long userId)
            throws Exception {
        Issues issues = issueService.addUserToIssue(issueId, userId);
        return ResponseEntity.ok(issues);
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issues> updateIssueStatus(@PathVariable String status,
                                                    @PathVariable Long issueId) throws Exception {
        Issues issues = issueService.updateStatus(issueId, status);
        return ResponseEntity.ok(issues);
    }
}