package com.example.aigatewayservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "thread_id")
    private String threadId;

    @Column(name = "sender_id")
    private Long senderId;

    private String content;

    private String attachmentFileName;
    private String attachmentOriginalFileName;

    @CreationTimestamp
    private LocalDateTime timestamp;
}