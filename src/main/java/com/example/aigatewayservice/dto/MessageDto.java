package com.example.aigatewayservice.dto;

import com.example.aigatewayservice.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private SenderDto sender;
    private String content;
    private String timestamp;

    // 내부 Sender DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SenderDto {
        private Long id;
        private String username;
        // private String avatarUrl; // 향후 프로필 이미지 URL 등 추가 가능
    }

    public static MessageDto fromEntity(Message message) {
        // TODO: 현재는 임시로 사용자 이름을 생성하지만, 향후 senderId로 실제 사용자 정보를 조회해야 합니다.
        SenderDto senderDto = SenderDto.builder()
                .id(message.getSenderId())
                .username("User " + message.getSenderId())
                .build();

        return MessageDto.builder()
                .id(message.getId())
                .sender(senderDto)
                .content(message.getContent())
                .timestamp(message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();
    }
}