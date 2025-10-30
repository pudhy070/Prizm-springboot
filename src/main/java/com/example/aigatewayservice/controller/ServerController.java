package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.entity.Server;
import com.example.aigatewayservice.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public List<Server> getAllServers() {
        return serverService.findAllServers();
    }

    /**
     * 새로운 서버를 생성합니다.
     * @param server 요청 본문에 포함된 서버 정보 (name, channels 등)
     * @return 생성된 서버 정보와 201 Created 상태 코드
     */
    @PostMapping
    public ResponseEntity<Server> createServer(@RequestBody Server server) {
        Server createdServer = serverService.createServer(server);
        // 생성된 리소스의 위치를 Location 헤더에 담아 반환 (RESTful API 권장사항)
        URI location = URI.create("/api/servers/" + createdServer.getId());
        return ResponseEntity.created(location).body(createdServer);
    }

    /**
     * 서버 정보를 업데이트합니다. (이름, 아이콘 등)
     * @param id 서버 ID
     * @param name 변경할 서버 이름
     * @param iconFile 변경할 아이콘 파일
     * @return 업데이트된 서버 정보
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Server> updateServer(
            @PathVariable Long id,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "iconFile", required = false) MultipartFile iconFile) {

        Server updatedServer = serverService.updateServer(id, name, iconFile);
        return ResponseEntity.ok(updatedServer);
    }

    /**
     * 요청하신 DELETE /api/servers/{id} 요청을 처리하는 엔드포인트입니다.
     * @param id 삭제할 서버의 ID
     * @return 성공 시 204 No Content, 서버가 없으면 404 Not Found 상태 코드
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }
}