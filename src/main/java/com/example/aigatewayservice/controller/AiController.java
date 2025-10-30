package com.example.aigatewayservice.controller;

import com.example.aigatewayservice.dto.AiChatRequest;
import com.example.aigatewayservice.dto.AiChatResponse;
import com.example.aigatewayservice.dto.SummarizeRequest;
import com.example.aigatewayservice.entity.Message;
import com.example.aigatewayservice.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * AI 챗봇과 대화합니다.
     * @param request 채널 ID와 메시지 내용
     * @return AI의 응답
     */
    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> getAiChatResponse(@RequestBody AiChatRequest request) {
        String aiReply = aiService.getChatResponse(request.getMessage());
        return ResponseEntity.ok(new AiChatResponse(aiReply));
    }

    /**
     * 업로드된 파일을 분석하고 요약 결과를 반환합니다.
     * @param file 분석할 파일
     * @return 파일 분석 요약 결과
     */
    @PostMapping("/analyze-file")
    public ResponseEntity<Map<String, String>> analyzeFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        String summary = aiService.analyzeFile(file);
        return ResponseEntity.ok(Map.of("summary", summary));
    }

    /**
     * 특정 스레드의 메시지를 요약합니다.
     * @param request 요약할 스레드 ID
     * @return AI 요약봇이 생성한 메시지 객체
     */
    @PostMapping("/summarize-thread")
    public ResponseEntity<Message> summarizeThread(@RequestBody SummarizeRequest request) {
        Message summaryMessage = aiService.summarizeThread(request.getThreadId());
        return ResponseEntity.ok(summaryMessage);
    }

    /**
     * AI 메시지 어시스턴트 기능을 제공합니다. (문장 다듬기, 톤 변경 등)
     * @param payload 원본 텍스트와 액션(improve, formal 등)
     * @return AI가 제안하는 수정된 텍스트
     */
    @PostMapping("/assist-message")
    public ResponseEntity<Map<String, String>> assistMessage(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        String action = payload.get("action");
        String suggestion = aiService.assistMessage(text, action);
        return ResponseEntity.ok(Map.of("suggestion", suggestion));
    }
}