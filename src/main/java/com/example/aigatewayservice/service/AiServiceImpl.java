package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Message;
import com.example.aigatewayservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate;
    private final MessageRepository messageRepository; // 스레드 요약에 필요

    @Value("${fastapi.server.url}")
    private String fastapiUrl;

    @Override
    public String getChatResponse(String message) {
        String url = fastapiUrl + "/ai/chat";
        Map<String, String> request = Map.of("message", message);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<>() {});
        Map<String, String> response = responseEntity.getBody();
        return response != null && response.containsKey("reply") ? response.get("reply") : "AI 응답을 받지 못했습니다.";
    }

    @Override
    public String analyzeFile(MultipartFile file) throws IOException {
        String url = fastapiUrl + "/ai/analyze-file";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
        Map<String, String> response = responseEntity.getBody();
        return response != null && response.containsKey("summary") ? response.get("summary") : "파일 분석 결과를 받지 못했습니다.";
    }

    @Override
    public Message summarizeThread(String threadId) {
        // 1. DB에서 스레드 ID에 해당하는 메시지들을 가져옵니다.
        //    MessageRepository에 findByThreadId(String threadId) 메서드가 정의되어 있어야 합니다.
        List<Message> messages = messageRepository.findByThreadId(threadId);
        String conversation = messages.stream()
                .map(msg -> "User " + msg.getSenderId() + ": " + msg.getContent())
                .collect(Collectors.joining("\n"));

        // 2. FastAPI로 요약 요청을 보냅니다.
        String url = fastapiUrl + "/ai/summarize";
        Map<String, String> request = Map.of("text", conversation);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<>() {});
        Map<String, String> response = responseEntity.getBody();
        String summaryContent = response != null && response.containsKey("summary") ? response.get("summary") : "요약에 실패했습니다.";

        // 3. 요약 결과를 새로운 Message 객체로 만들어 반환합니다.
        //    AI 봇의 고정된 ID와 이름을 사용합니다.
        Message summaryMessage = new Message();
        summaryMessage.setSenderId(0L); // AI 봇의 고정 ID (예: 0)
        summaryMessage.setThreadId(threadId); // 동일한 스레드에 속하도록 설정
        summaryMessage.setContent("✨ **스레드 요약:**\n" + summaryContent);
        return summaryMessage;
    }

    @Override
    public String assistMessage(String text, String action) {
        String url = fastapiUrl + "/ai/assist-message";
        Map<String, String> request = Map.of("text", text, "action", action);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<>() {});
        Map<String, String> response = responseEntity.getBody();
        return response != null && response.containsKey("suggestion") ? response.get("suggestion") : text;
    }
}