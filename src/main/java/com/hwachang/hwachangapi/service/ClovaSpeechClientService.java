package com.hwachang.hwachangapi.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hwachang.hwachangapi.entity.NestRequestEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class ClovaSpeechClientService {

    @Value("${clova.speech.secret-key}")
    private String secretKey;

    @Value("${clova.speech.invoke-url}")
    private String invokeUrl;

    @Value("${naver.api.client-id}")
    private String naverClientId;

    @Value("${naver.api.client-secret}")
    private String naverClientSecret;

    @Value("${naver.api.summary-url}")
    private String summaryUrl;

    private final Gson gson = new Gson();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String recognizeFile(MultipartFile file) throws IOException {
        System.out.println("Using secret key: " + secretKey); // 확인용
        HttpPost httpPost = new HttpPost(invokeUrl + "/recognizer/upload");
        httpPost.addHeader(new BasicHeader("X-CLOVASPEECH-API-KEY", secretKey));

        // Create the request parameters entity
        NestRequestEntity requestEntity = new NestRequestEntity();
        requestEntity.setLanguage("ko-KR");
        requestEntity.setCompletion("sync");
        requestEntity.setFullText(true);

        // Build the multipart entity
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", gson.toJson(requestEntity), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", file.getInputStream(), ContentType.DEFAULT_BINARY, file.getOriginalFilename())
                .build();

        httpPost.setEntity(httpEntity);
        return executeRequest(httpPost);
    }

    public String summarizeText(String jsonResponse) throws IOException {
        // JSON 응답에서 textEdited 값을 추출하여 결합
        String concatenatedTextEdited = extractEditedText(jsonResponse);

        // 요약 API 요청 설정
        HttpPost httpPost = new HttpPost(summaryUrl);
        httpPost.addHeader("X-NCP-APIGW-API-KEY-ID", naverClientId);
        httpPost.addHeader("X-NCP-APIGW-API-KEY", naverClientSecret);
        httpPost.addHeader("Content-Type", "application/json");

        // 요약 API에 맞는 요청 본문 작성
        JsonObject requestBody = new JsonObject();

        JsonObject document = new JsonObject();
        document.addProperty("content", concatenatedTextEdited);  // 추출된 텍스트 추가
        requestBody.add("document", document);

        JsonObject option = new JsonObject();
        option.addProperty("language", "ko");
        option.addProperty("model", "general");
        option.addProperty("summaryCount", 3);
        requestBody.add("option", option);

        // JSON 요청을 StringEntity로 생성하여 설정
        String jsonRequest = gson.toJson(requestBody);
        HttpEntity entity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        // 요약된 결과 반환
        return executeRequest(httpPost);
    }



    /**
     * JSON 응답에서 segments 배열의 textEdited 값을 모두 추출하고 하나의 문자열로 연결하는 메서드
     */
    private String extractEditedText(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray segments = jsonObject.getAsJsonArray("segments");

        StringJoiner textEditedJoiner = new StringJoiner(" ");
        for (int i = 0; i < segments.size(); i++) {
            JsonObject segment = segments.get(i).getAsJsonObject();
            if (segment.has("textEdited")) {
                textEditedJoiner.add(segment.get("textEdited").getAsString());
            }
        }

        return textEditedJoiner.toString();
    }




    private String executeRequest(HttpPost httpPost) {
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error while calling Clova Speech API", e);
        }
    }
}
