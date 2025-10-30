package com.example.aigatewayservice.repository;

import com.example.aigatewayservice.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByServerId(Long serverId);
}