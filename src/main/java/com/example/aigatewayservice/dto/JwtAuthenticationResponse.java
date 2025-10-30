package com.example.aigatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
}