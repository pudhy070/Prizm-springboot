package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.dto.MessageDto;
import com.example.aigatewayservice.entity.Message;
import com.example.aigatewayservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    // WebSocket을 통해 메시지를 특정 토픽으로 보낼 때 사용하는 템플릿
    private final SimpMessagingTemplate messagingTemplate;
    // 메시지 관련 비즈니스 로직을 처리하는 서비스
    private final MessageService messageService;

    /**
     * 클라이언트가 '/app/chat'으로 메시지를 보내면 이 메서드가 처리합니다.
     * (WebSocketConfig에서 setApplicationDestinationPrefixes("/app")으로 설정했기 때문)
     *
     * @param message 클라이언트로부터 받은 메시지 데이터
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {
        // 1. 서비스를 통해 메시지를 저장 (타임스탬프 설정은 서비스에서 처리)
        Message savedMessage = messageService.saveMessage(message);

        // 2. 저장된 엔티티를 DTO로 변환
        MessageDto messageDto = MessageDto.fromEntity(savedMessage);

        // 3. 해당 채널을 구독하는 모든 클라이언트에게 DTO 형식으로 메시지를 전송
        String destination = "/topic/servers/" + savedMessage.getServerId() + "/channels/" + savedMessage.getChannelId();
        messagingTemplate.convertAndSend(destination, messageDto);
    }
}