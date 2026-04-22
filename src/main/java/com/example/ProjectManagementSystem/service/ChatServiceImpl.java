package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Chat;
import com.example.ProjectManagementSystem.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepository chatRepository;


    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }
}
