package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Channel;

import java.util.List;

public interface ChannelService {
    List<Channel> findChannelsByServerId(Long serverId);
    Channel createChannel(Long serverId, Channel channel);
}