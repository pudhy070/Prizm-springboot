package com.example.aigatewayservice.repository;

import com.example.aigatewayservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByThreadId(String threadId);
    List<Message> findByChannelIdOrderByTimestampAsc(Long channelId);
}