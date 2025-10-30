package com.example.aigatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프론트엔드(SockJS)가 WebSocket 연결을 시작할 엔드포인트를 등록합니다.
        // '/api/ws' 경로를 설정하면 '/api/ws/info' 요청을 자동으로 처리합니다.
        registry.addEndpoint("/api/ws")
                // CORS 문제를 해결하기 위해 프론트엔드 오리진(http://localhost:3000)을 허용합니다.
                .setAllowedOriginPatterns("http://localhost:3000")
                // WebSocket을 지원하지 않는 브라우저를 위해 SockJS를 활성화합니다.
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커가 /topic으로 시작하는 주소를 구독하는 클라이언트에게 메시지를 전달하도록 설정합니다.
        registry.enableSimpleBroker("/topic");
        // 클라이언트가 서버로 메시지를 보낼 때 사용하는 주소의 접두사입니다. (e.g., @MessageMapping("/chat"))
        registry.setApplicationDestinationPrefixes("/app");
    }
}