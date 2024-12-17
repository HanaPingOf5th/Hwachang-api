package com.hwachang.hwachangapi.utils.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClovaSpeechProvider {

    @Value("${clova.speech.secret-key}")
    private String secretKey;

    @Value("${clova.speech.invoke-url}")
    private String invokeUrl;

    @Value("${clova.api.host}")
    private String apiHost;

    @Value("${clova.api.key}")
    private String apiKey;

    @Value("${clova.api.gateway-key}")
    private String gatewayKey;

    @Value("${clova.api.request-id}")
    private String requestId;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final RestTemplate restTemplate = new RestTemplate();

    public String SummarizeAudio(InputStream fileStream, String fileName) throws IOException {
        // 1. 음성 파일을 텍스트로 변환 (JSON 형태 반환)
        String sttResponse = recognizeFile(fileStream, fileName);

        // 2. JSON 파싱: 맨 마지막 통합된 "text" 추출
        JsonObject jsonObject = JsonParser.parseString(sttResponse).getAsJsonObject();
        String finalText = jsonObject.get("text").getAsString();

        return finalText;
    }

    private String executeRequest(HttpPost httpPost) throws IOException {
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
    }

    public String recognizeFile(InputStream fileStream, String fileName) throws IOException {
        HttpPost httpPost = new HttpPost(invokeUrl + "/recognizer/upload");
        httpPost.addHeader("X-CLOVASPEECH-API-KEY", secretKey);

        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("language", "ko-KR");
        requestParams.addProperty("completion", "sync");
        requestParams.addProperty("fullText", true);
        requestParams.addProperty("format", "JSON");

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", requestParams.toString(), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", fileStream, ContentType.DEFAULT_BINARY, fileName)
                .build();

        httpPost.setEntity(httpEntity);
        return executeRequest(httpPost);
    }

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

        org.springframework.http.HttpEntity<Map<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, requestEntity, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
        Map<String, Object> message = (Map<String, Object>) result.get("message");

        return (String) message.get("content");
    }
}
