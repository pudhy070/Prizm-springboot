package com.example.aigatewayservice.service;

import com.example.aigatewayservice.dto.MessageDto;
import com.example.aigatewayservice.entity.Message;
import com.example.aigatewayservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageDto> findMessagesByChannelId(Long channelId) {
        List<Message> messages = messageRepository.findByChannelIdOrderByTimestampAsc(channelId);
        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Message saveMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}