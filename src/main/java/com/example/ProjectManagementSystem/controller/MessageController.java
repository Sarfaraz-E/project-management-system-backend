package com.example.ProjectManagementSystem.controller;

import com.example.ProjectManagementSystem.config.request.CreateMessageRequest;
import com.example.ProjectManagementSystem.model.Chat;
import com.example.ProjectManagementSystem.model.Message;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.service.MessageService;
import com.example.ProjectManagementSystem.service.ProjectService;
import com.example.ProjectManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "06 - Messaging", description = "Messaging APIs")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest request)
        throws Exception {
        User user = userService.findUserById(request.getSenderId());

        Chat chats = projectService.getProjectById(request.getProjectId()).getChat();

        if (chats == null) throw  new Exception("Chats not Found");

        Message sendMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());
        return ResponseEntity.ok(sendMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessageByChatId(@PathVariable Long projectId) throws Exception{
        List<Message> messages = messageService.getMesdsagesByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }

}
