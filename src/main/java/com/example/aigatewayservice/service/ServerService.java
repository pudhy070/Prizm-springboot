package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Server;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServerService {
    List<Server> findAllServers();
    Server createServer(Server server);
    Server updateServer(Long id, String name, MultipartFile iconFile);
    void deleteServer(Long id);
}