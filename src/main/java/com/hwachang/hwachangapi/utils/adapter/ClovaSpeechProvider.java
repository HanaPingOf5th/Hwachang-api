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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    public String summarizeTextFromSTTResponse(String sttResponseJson) throws IOException {
        String[] textArray = extractTextEditedAsArray(sttResponseJson);
        return summarizeText(textArray);
    }

    private String[] extractTextEditedAsArray(String sttResponseJson) {
        JsonObject jsonObject = JsonParser.parseString(sttResponseJson).getAsJsonObject();
        JsonArray segments = jsonObject.getAsJsonArray("segments");

        String[] textArray = new String[segments.size()];
        for (int i = 0; i < segments.size(); i++) {
            JsonObject segment = segments.get(i).getAsJsonObject();
            textArray[i] = segment.get("textEdited").getAsString();
        }
        return textArray;
    }

    private String summarizeText(String[] inputTexts) throws IOException {
        HttpPost httpPost = new HttpPost(apiHost + "/testapp/v1/api-tools/summarization/v2/24ae2857401c4e04904543a246b12805");
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.addHeader("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        httpPost.addHeader("X-NCP-APIGW-API-KEY", gatewayKey);
        httpPost.addHeader("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId);

        JsonObject requestBody = new JsonObject();
        JsonArray textsArray = new JsonArray();

        for (String text : inputTexts) {
            textsArray.add(text);
        }

        requestBody.add("texts", textsArray);
        requestBody.addProperty("segMinSize", 300);
        requestBody.addProperty("includeAiFilters", true);
        requestBody.addProperty("autoSentenceSplitter", true);
        requestBody.addProperty("segCount", -1);
        requestBody.addProperty("segMaxSize", 1000);

        HttpEntity entity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        return executeRequest(httpPost);
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
}
