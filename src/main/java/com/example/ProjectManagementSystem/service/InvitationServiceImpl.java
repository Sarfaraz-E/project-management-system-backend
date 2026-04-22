package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Invitation;
import com.example.ProjectManagementSystem.repository.InvitationRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;

    @SneakyThrows
    @Override
    // UPDATED: Added targetRoleId parameter
    public void sendInvitation(String email, Long projectId, Long targetRoleId) {

        String invitationToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);

        // NEW: Save the target role to the invitation
        invitation.setTargetRoleId(targetRoleId);

        invitationRepository.save(invitation);

        // FIXED TYPO: Added the "=" sign before the token
        String invitationLink = "http://localhost:5173/accept_invitation?token=" + invitationToken;
        emailService.sendEmailWithToken(email, invitationLink);
    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) throws Exception {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new Exception("Invalid Invitation Token");
        }

        // NEW: Delete the invitation after it is successfully retrieved so it cannot be reused.
        // We delete it here because the ProjectController already has all the data it needs
        // to assign the user to the project in the next step.
        invitationRepository.delete(invitation);

        return invitation;
    }

    @Override
    public String getTokenByUserMail(String userEmail) {
        Invitation invitation = invitationRepository.findByEmail(userEmail);
        return invitation != null ? invitation.getToken() : null;
    }

    @Override
    public void deleteToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation != null) {
            invitationRepository.delete(invitation);
        }
    }
}