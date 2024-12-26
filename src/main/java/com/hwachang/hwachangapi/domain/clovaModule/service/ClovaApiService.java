package com.hwachang.hwachangapi.domain.clovaModule.service;

import com.google.gson.JsonObject;
import com.hwachang.hwachangapi.domain.clovaModule.utils.FileUtils;
import com.hwachang.hwachangapi.utils.adapter.LLMServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClovaApiService {

    private final LLMServicePort llmServicePort;

    public String recognizeAll(String fileUrl) throws IOException {
        InputStream inputStream = FileUtils.downloadFileAsStream(fileUrl);
        return llmServicePort.transferAudioToTextAll(inputStream, fileUrl);
    }

    public List<Map<String, Object>>  recognizeAudio(String fileUrl) throws IOException {
        InputStream inputStream = FileUtils.downloadFileAsStream(fileUrl);
        return llmServicePort.transferAudioToText(inputStream, fileUrl);
    }

    public String processAndSummarizeAudio(String fileUrl) throws IOException {
        InputStream inputStream = FileUtils.downloadFileAsStream(fileUrl);
        return llmServicePort.processAndSummarizeAudio(inputStream, fileUrl);
    }
}
