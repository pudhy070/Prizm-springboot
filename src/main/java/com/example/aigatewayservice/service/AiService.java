package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AiService {
    String getChatResponse(String message);
    String analyzeFile(MultipartFile file) throws IOException;
    Message summarizeThread(String threadId);
    String assistMessage(String text, String action);
}