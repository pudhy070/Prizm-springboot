package com.example.aigatewayservice.dto;

import lombok.Data;

@Data
public class CreateChannelRequest {
    private String name;
    private String type; // e.g., "TEXT", "VOICE"
    // 향후 카테고리 ID 등 추가 가능
}