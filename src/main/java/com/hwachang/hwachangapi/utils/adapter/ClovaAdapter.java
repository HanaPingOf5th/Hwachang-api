package com.hwachang.hwachangapi.utils.adapter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ClovaAdapter implements LLMServicePort {

    private final ClovaSpeechProvider clovaSpeechProvider;

    public ClovaAdapter(ClovaSpeechProvider clovaSpeechProvider) {
        this.clovaSpeechProvider = clovaSpeechProvider;
    }

    @Override
    public String transferAudioToText(InputStream fileStream, String fileName) throws IOException {
        return clovaSpeechProvider.recognizeFile(fileStream, fileName);
    }

    @Override
    public String processAndSummarizeAudio(InputStream fileStream, String fileName) throws IOException {
        // 1. 음성 파일을 텍스트로 변환
        String sttResponse = clovaSpeechProvider.SummarizeAudio(fileStream, fileName);

        // 2. 텍스트를 SYSTEM_MESSAGE_CONTENT에 맞춰 AI 요약 API 호출
        return clovaSpeechProvider.callClovaApi(sttResponse);
    }
}
