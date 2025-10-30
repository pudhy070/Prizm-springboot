package com.example.aigatewayservice.dto;

import lombok.Data;

@Data
public class CreateServerRequest {
    private String name;
    // 향후 소유자 ID 등 추가 가능
}