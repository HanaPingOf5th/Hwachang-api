package com.hwachang.hwachangapi.service;

import com.google.gson.Gson;
import com.hwachang.hwachangapi.entity.NestRequestEntity;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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

@Service
@RequiredArgsConstructor
public class ClovaSpeechClientService {

    @Value("${clova.speech.secret-key}")
    private String secretKey;

    @Value("${clova.speech.invoke-url}")
    private String invokeUrl;

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

    private String executeRequest(HttpPost httpPost) {
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error while calling Clova Speech API", e);
        }
    }
}
