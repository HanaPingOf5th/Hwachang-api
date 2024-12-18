package com.hwachang.hwachangapi.utils.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClovaAdapter implements LLMServicePort {

    private final ClovaSpeechProvider clovaSpeechProvider;

    public ClovaAdapter(ClovaSpeechProvider clovaSpeechProvider) {
        this.clovaSpeechProvider = clovaSpeechProvider;
    }

    @Override
    public List<Map<String, Object>> transferAudioToText(InputStream fileStream, String fileName) throws IOException {
        List<JsonObject> jsonObjects = clovaSpeechProvider.parseSegments(fileStream, fileName);

        // JsonObject를 Map<String, Object>로 변환
        List<Map<String, Object>> result = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            Map<String, Object> map = new Gson().fromJson(jsonObject, Map.class);
            result.add(map);
        }
        return result;
    }

    @Override
    public String processAndSummarizeAudio(InputStream fileStream, String fileName) throws IOException {
        // 1. 음성 파일을 텍스트로 변환
        String sttResponse = clovaSpeechProvider.SummarizeAudio(fileStream, fileName);

        // 2. 텍스트를 SYSTEM_MESSAGE_CONTENT에 맞춰 AI 요약 API 호출
        return clovaSpeechProvider.callClovaApi(sttResponse);
    }
}
