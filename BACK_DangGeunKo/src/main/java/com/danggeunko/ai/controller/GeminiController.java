package com.danggeunko.ai.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danggeunko.ai.dto.ChatRequest;
import com.danggeunko.ai.service.GeminiService;


@RestController
@RequestMapping("/api-ai")
@CrossOrigin("*") 
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody ChatRequest request) {
        // 1. DTO에서 메시지 추출
        String userMessage = request.getMessage();
        
        // 2. 서비스 호출 (GMS API 통신)
        String answer = geminiService.getResponse(userMessage);
        
        // 3. 응답 반환
        return ResponseEntity.ok(Map.of("answer", answer));
    }
}