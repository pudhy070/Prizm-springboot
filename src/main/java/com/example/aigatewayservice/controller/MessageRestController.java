package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.dto.MessageDto;
import com.example.aigatewayservice.entity.Message;
import com.example.aigatewayservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/{channelId}")
    public List<MessageDto> getMessagesByChannelId(@PathVariable Long channelId) {
        return messageService.findMessagesByChannelId(channelId);
    }
}