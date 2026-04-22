package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageService {

    Message sendMessage(Long senderId, Long chatId, String content) throws Exception;

    List<Message> getMesdsagesByProjectId(Long projectId) throws Exception;
}
