package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Server;
import com.example.aigatewayservice.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final MinioService minioService;

    @Override
    public List<Server> findAllServers() {
        return serverRepository.findAll();
    }

    @Override
    @Transactional
    public Server createServer(Server server) {
        // 아이콘 파일 처리 로직이 필요하다면 여기에 추가
        return serverRepository.save(server);
    }

    @Override
    @Transactional
    public Server updateServer(Long id, String name, MultipartFile iconFile) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Server not found with id: " + id));
        if (name != null && !name.isEmpty()) {
            server.setName(name);
        }

        if (iconFile != null && !iconFile.isEmpty()) {
            try {
                String iconUrl = minioService.uploadFile(iconFile, "server-icons");
                server.setIconUrl(iconUrl);
            } catch (IOException e) {
                // 실제 프로덕션 코드에서는 로깅 및 구체적인 예외 처리가 필요합니다.
                throw new RuntimeException("Failed to upload icon file", e);
            }
        }
        return serverRepository.save(server);
    }

    @Override
    @Transactional
    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    }
}