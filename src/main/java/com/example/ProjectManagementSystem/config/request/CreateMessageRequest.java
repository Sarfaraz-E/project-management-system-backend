package com.example.ProjectManagementSystem.config.request;

import lombok.Data;

@Data
public class CreateMessageRequest {

    private Long senderId;

    private String content;

    private Long projectId;
}
