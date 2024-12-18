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
import java.util.ArrayList;
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

    @Value("${clova.api.url}")
    private String apiUrl;

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

        return jsonObject.get("text").getAsString();
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
                    "   - 특수문자는 절대 사용하지 말아줘.\n" +
                    "   - 문장 간결하고 핵심만 전달하도록 작성해.\n" +
                    "   - 대화의 의미를 해치지 않도록 자연스러운 흐름을 유지해줘.";



    public String callClovaApi(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        headers.set("X-NCP-APIGW-API-KEY", gatewayKey);
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

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
        Map<String, Object> message = (Map<String, Object>) result.get("message");

        return (String) message.get("content");
    }

    /**
     * 음성 파일을 인식하고 필요한 데이터를 JSON 형태로 반환
     */
    public List<JsonObject> parseSegments(InputStream fileStream, String fileName) throws IOException {
        // 1. 음성 파일을 JSON 형태로 변환
        String sttResponse = recognizeFile(fileStream, fileName);

        // 2. JSON 파싱: segments 배열 추출
        JsonObject jsonObject = JsonParser.parseString(sttResponse).getAsJsonObject();
        JsonArray segments = jsonObject.getAsJsonArray("segments");

        // 3. 필요한 데이터만 가공하여 반환
        List<JsonObject> parsedData = new ArrayList<>();

        for (int i = 0; i < segments.size(); i++) {
            JsonObject segment = segments.get(i).getAsJsonObject();

            // start와 end 시간 변환
            int startTimeMs = segment.get("start").getAsInt();
            int endTimeMs = segment.get("end").getAsInt();

            String startTime = convertMsToTime(startTimeMs);
            String endTime = convertMsToTime(endTimeMs);

            // text와 diarization 정보 추출
            String text = segment.get("text").getAsString();
            String speaker = segment.getAsJsonObject("diarization").get("label").getAsString();

            // 새로운 JSON 객체에 데이터 추가
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("startTime", startTime);
            resultObject.addProperty("endTime", endTime);
            resultObject.addProperty("text", text);
            resultObject.addProperty("speaker", speaker);

            parsedData.add(resultObject);
        }

        return parsedData;
    }

    /**
     * 밀리초(ms)를 시간, 분, 초 형식으로 변환
     */
    private String convertMsToTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
