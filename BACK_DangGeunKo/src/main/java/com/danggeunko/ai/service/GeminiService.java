package com.danggeunko.ai.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.danggeunko.ai.dto.GeminiResponse;

@Service
public class GeminiService {

    @Value("${gms.api.url}")
    private String apiUrl;

    @Value("${gms.api.key}")
    private String apiKey;

    private final RestClient restClient;

    public GeminiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String getResponse(String message) {
        // 요청 바디 생성
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", message)
                ))
            )
        );

        try {
            // GMS API 호출 및 DTO 매핑
            GeminiResponse response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(GeminiResponse.class);

            // Null 체크를 곁들인 텍스트 추출
            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0)
                               .getContent()
                               .getParts().get(0)
                               .getText();
            }
            return "응답 결과가 없습니다.";
        } catch (Exception e) {
            return "API 통신 중 오류 발생: " + e.getMessage();
        }
    }
}