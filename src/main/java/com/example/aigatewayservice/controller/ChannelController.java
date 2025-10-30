package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.entity.Channel;
import com.example.aigatewayservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servers/{serverId}/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public List<Channel> getChannelsByServerId(@PathVariable Long serverId) {
        return channelService.findChannelsByServerId(serverId);
    }

    @PostMapping
    public ResponseEntity<Channel> createChannel(@PathVariable Long serverId, @RequestBody Channel channel) {
        Channel createdChannel = channelService.createChannel(serverId, channel);
        URI location = URI.create(String.format("/api/servers/%d/channels/%d", serverId, createdChannel.getId()));
        return ResponseEntity.created(location).body(createdChannel);
    }
}