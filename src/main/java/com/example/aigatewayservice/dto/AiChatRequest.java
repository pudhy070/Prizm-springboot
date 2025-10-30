package com.example.aigatewayservice.dto;

import lombok.Data;

@Data
public class AiChatRequest {
    private Long channelId;
    private String message;
}