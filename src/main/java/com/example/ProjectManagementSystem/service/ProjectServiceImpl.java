package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Chat;
import com.example.ProjectManagementSystem.model.Project;
import com.example.ProjectManagementSystem.model.ProjectMember;
import com.example.ProjectManagementSystem.model.ProjectRole;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.repository.ProjectMemberRepository;
import com.example.ProjectManagementSystem.repository.ProjectRepository;
import com.example.ProjectManagementSystem.repository.ProjectRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    // NEW REPOSITORIES INJECTED
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRoleRepository projectRoleRepository;

    @Override
    public Project createProject(Project project, User user) throws Exception {
        Project createdProject = new Project();

        createdProject.setOwner(user);
        createdProject.setTag(project.getTag());
        createdProject.setName(project.getName());
        createdProject.setCategory(project.getCategory());
        createdProject.setDescription(project.getDescription());

        // REMOVED: createdProject.getTeam().add(user);

        Project savedProject = projectRepository.save(createdProject);

        // NEW LOGIC: Assign the creator the "PROJECT_OWNER" role in the junction table
        ProjectRole ownerRole = projectRoleRepository.findByName("PROJECT_OWNER")
                .orElseThrow(() -> new Exception("Role 'PROJECT_OWNER' not found. Please run your database seeder."));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setUser(user);
        projectMember.setProject(savedProject);
        projectMember.setRole(ownerRole);
        projectMemberRepository.save(projectMember);

        // Set up Chat
        Chat chat = new Chat();
        chat.setProject(savedProject);
        Chat projectChat = chatService.createChat(chat);

        savedProject.setChat(projectChat);
        return projectRepository.save(savedProject); // Save again to map the chat
    }

    @Override
    public List<Project> getProjectByTeam(User user, String category, String tag) throws Exception {
        // NEW LOGIC: Fetch all memberships for this user, then extract the projects
        List<ProjectMember> memberships = projectMemberRepository.findByUserId(user.getId());
        List<Project> projects = memberships.stream()
                .map(ProjectMember::getProject)
                .distinct() // Ensure no duplicates
                .collect(Collectors.toList());

        // Filtering categories of projects
        if (category != null) {
            projects = projects.stream()
                    .filter(project -> project.getCategory() != null && project.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        // For projects tag filtering
        if (tag != null) {
            projects = projects.stream()
                    .filter(project -> project.getTag() != null && project.getTag().contains(tag))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new Exception("Project not Found");
        }
        return optionalProject.get();
    }

    @Override
    public void deleteProject(Long projectId, Long userId) throws Exception {
        getProjectById(projectId); // Just to verify it exists
        projectRepository.deleteById(projectId);
    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws Exception {
        Project project = getProjectById(id);
        project.setName(updatedProject.getName());
        project.setTag(updatedProject.getTag());
        project.setDescription(updatedProject.getDescription());

        return projectRepository.save(project);
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {

    }

    // UPDATED SIGNATURE: We must pass a target role ID when adding a user now!
    // NOTE: You MUST update your ProjectService interface to include Long targetRoleId
    public void addUserToProject(Long projectId, Long userId, Long targetRoleId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);

        // Check if they are already a member
        Optional<ProjectMember> existingMember = projectMemberRepository.findByUserIdAndProjectId(userId, projectId);

        if (existingMember.isEmpty()) {
            ProjectRole assignedRole = projectRoleRepository.findById(targetRoleId)
                    .orElseThrow(() -> new Exception("Role not found"));

            ProjectMember newMember = new ProjectMember();
            newMember.setUser(user);
            newMember.setProject(project);
            newMember.setRole(assignedRole);
            projectMemberRepository.save(newMember);

            // Add them to the chat as well
            if (project.getChat() != null) {
                project.getChat().getUsers().add(user);
                projectRepository.save(project); // Saves the chat mapping
            }
        }
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);

        // NEW LOGIC: Find the junction record and delete it
        Optional<ProjectMember> memberOpt = projectMemberRepository.findByUserIdAndProjectId(userId, projectId);

        if (memberOpt.isPresent()) {
            projectMemberRepository.delete(memberOpt.get());

            // Remove from chat
            if (project.getChat() != null) {
                project.getChat().getUsers().remove(user);
                projectRepository.save(project);
            }
        }
    }

    @Override
    public Chat getChatByProject(Long projectId) throws Exception {
        Project project = getProjectById(projectId);
        return project.getChat();
    }

    @Override
    public List<Project> searchProjects(String keyword, User user) throws Exception {
        // NEW LOGIC: Get all user's projects via memberships, then filter by keyword
        List<ProjectMember> memberships = projectMemberRepository.findByUserId(user.getId());

        return memberships.stream()
                .map(ProjectMember::getProject)
                .filter(project -> project.getName() != null && project.getName().toLowerCase().contains(keyword.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }
}