package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.dto.JwtAuthenticationResponse;
import com.example.aigatewayservice.dto.SignInRequest;
import com.example.aigatewayservice.dto.SignUpRequest;
import com.example.aigatewayservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        authenticationService.signup(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    /**
     * 소셜 로그인 시작을 위한 엔드포인트.
     * 클라이언트는 /api/auth/oauth2/{provider}?redirect_uri=... 로 요청합니다.
     * e.g., /api/auth/oauth2/google
     * 이 요청은 Spring Security의 OAuth2 로그인 필터 체인으로 연결됩니다.
     * 실제 로직은 SecurityConfig에 설정된 대로 동작하므로, 컨트롤러에 별도 구현은 필요 없습니다.
     * 이 주석은 설명을 위해 존재합니다.
     */

}