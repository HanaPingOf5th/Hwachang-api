package com.hwachang.hwachangapi.domain.clovaModule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClovaApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SYSTEM_MESSAGE_CONTENT =
            "주어진 상담원문을 아래 조건에 맞게 수정해줘:\n" +
                    "1. **주요주제 작성**:\n" +
                    "   - 상담원문의 핵심 주제를 파악해서 '주요주제: [주요 주제]' 형태로 반드시 작성해줘.\n" +
                    "2. **요약 내용 작성**:\n" +
                    "   - 요약은 한 문장씩 작성하고 **30자 이내**로 제한해.\n" +
                    "   - 각 요약 문장은 줄바꿈(개행)해서 나열해줘.\n" +
                    "3. **대화 흐름 반영**:\n" +
                    "   - 실제 고객과 상담원의 대화 흐름을 반영해야 해.\n" +
                    "4. **형식 및 출력 예시**:\n" +
                    "   주요주제: [핵심 주제]\n" +
                    "   - [요약문장1]\n" +
                    "   - [요약문장2]\n" +
                    "   - [요약문장3]\n" +
                    "5. **유의사항**:\n" +
                    "   - 특수문자는 절대 사용하지 말아줘.\n";

    private static final String API_URL = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003";

    public String callClovaApi(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", "NTA0MjU2MWZlZTcxNDJiY97QrrA6UMhe0PTH7P9CpKmtMwqOVj8p1U5/OhclIE6b");
        headers.set("X-NCP-APIGW-API-KEY", "y6bvnqr0gP7MpGAyp4GBOVBPaIQZg9nVoAARj8DD");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // 요청 Body 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("topP", 0.8);
        requestBody.put("maxTokens", 500);
        requestBody.put("temperature", 0.5);
        requestBody.put("repeatPenalty", 5.0);

        // System 메시지 설정
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_MESSAGE_CONTENT);

        // User 메시지 설정
        Map<String, Object> userMessageContent = new HashMap<>();
        userMessageContent.put("role", "user");
        userMessageContent.put("content", userMessage);

        requestBody.put("messages", List.of(systemMessage, userMessageContent));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, requestEntity, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
        Map<String, Object> message = (Map<String, Object>) result.get("message");

        return (String) message.get("content");
    }
}
