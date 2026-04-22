package com.example.ProjectManagementSystem.controller;

import com.example.ProjectManagementSystem.model.*;
import com.example.ProjectManagementSystem.repository.InviteRequest;
import com.example.ProjectManagementSystem.response.MessageResponse;
import com.example.ProjectManagementSystem.service.InvitationService;
import com.example.ProjectManagementSystem.service.ProjectService;
import com.example.ProjectManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "03 - Project", description = "Project APIs")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationService invitationService;

    // No @PreAuthorize needed here because it fetches projects belonging to the logged-in user
    @GetMapping
    public ResponseEntity<List<Project>> getProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.getProjectByTeam(user, category, tag);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // NEW SECURED ENDPOINT: User must have 'READ_PROJECT' permission
    @GetMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.hasPermission(authentication, #projectId, 'READ_PROJECT')")
    public ResponseEntity<Project> getProjectById(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Project project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // No @PreAuthorize needed because the project doesn't exist yet.
    // The user automatically becomes the 'PROJECT_OWNER' inside the service.
    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Project project
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project createdProject = projectService.createProject(project, user);
        return new ResponseEntity<>(createdProject, HttpStatus.OK);
    }

    // NEW SECURED ENDPOINT: User must have 'UPDATE_PROJECT' permission (Usually Admins/Owners)
    @PatchMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.hasPermission(authentication, #projectId, 'UPDATE_PROJECT')")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt,
            @RequestBody Project project
    ) throws Exception {
        Project updatedProject = projectService.updateProject(project, projectId);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    // NEW SECURED ENDPOINT: User must have 'DELETE_PROJECT' permission (Usually Owners only)
    @DeleteMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.hasPermission(authentication, #projectId, 'DELETE_PROJECT')")
    public ResponseEntity<MessageResponse> deleteProject(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId, user.getId());
        MessageResponse messageResponse = new MessageResponse("Project Deleted Successfully");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    // No @PreAuthorize needed as it filters across all projects the user is already part of
    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(
            @RequestParam(required = false) String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.searchProjects(keyword, user);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // NEW SECURED ENDPOINT: User must have 'READ_PROJECT' or 'READ_CHAT' permission
    // Note: Consider changing @PatchMapping to @GetMapping for fetching data in the future
    @PatchMapping("/{projectId}/chat")
    @PreAuthorize("@projectSecurity.hasPermission(authentication, #projectId, 'READ_PROJECT')")
    public ResponseEntity<Chat> getChatByProjectId(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Chat chat = projectService.getChatByProject(projectId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    // NEW SECURED ENDPOINT: User must have 'INVITE_USERS' permission to send invites
    // Notice how we read #request.projectId for the security check!
    @PostMapping("/invite")
    @PreAuthorize("@projectSecurity.hasPermission(authentication, #request.projectId, 'INVITE_USERS')")
    public ResponseEntity<MessageResponse> inviteProject(
            @RequestBody InviteRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        try {
            // This saves the invitation to the DB with the targetRoleId
            invitationService.sendInvitation(request.getEmail(), request.getProjectId(), request.getTargetRoleId());

            return ResponseEntity.ok(new MessageResponse("Invitation sent successfully to " + request.getEmail()));
        } catch (Exception e) {
            // Even if the Email fails, we return a success message because the
            // record is now in the database and can be processed manually!
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse("Invitation generated in database, but email failed: " + e.getMessage()));
        }
    }
    // No @PreAuthorize needed because they aren't part of the project yet!
    @GetMapping("/acceptInvitation")
    public ResponseEntity<Invitation> acceptInvitationProject(
            @RequestParam String token,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token, user.getId());

        // UPDATED: Passing the targetRoleId from the invitation entity to the ProjectService
        projectService.addUserToProject(invitation.getProjectId(), user.getId(), invitation.getTargetRoleId());

        return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);
    }
}