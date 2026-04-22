package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Invitation;

public interface InvitationService {

    // UPDATED: Added targetRoleId
    void sendInvitation(String email, Long projectId, Long targetRoleId) throws Exception;

    Invitation acceptInvitation(String token, Long userId) throws Exception;

    String getTokenByUserMail(String userEmail);

    void deleteToken(String token);
}