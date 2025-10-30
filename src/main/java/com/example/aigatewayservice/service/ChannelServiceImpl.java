package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Channel;
import com.example.aigatewayservice.entity.Server;
import com.example.aigatewayservice.repository.ChannelRepository;
import com.example.aigatewayservice.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;

    @Override
    public List<Channel> findChannelsByServerId(Long serverId) {
        return channelRepository.findByServerId(serverId);
    }

    @Override
    @Transactional
    public Channel createChannel(Long serverId, Channel channel) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new IllegalArgumentException("Server not found with id: " + serverId));
        channel.setServer(server);
        return channelRepository.save(channel);
    }
}