package com.example.aigatewayservice.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}