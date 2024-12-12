package com.hwachang.hwachangapi.utils.adapter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ClovaAdapter implements LLMServicePort {

    private final ClovaSpeechProvider clovaSpeechProvider;

    public ClovaAdapter(ClovaSpeechProvider clovaSpeechProvider) {
        this.clovaSpeechProvider = clovaSpeechProvider;
    }

    @Override
    public String transferAudioToText(MultipartFile file) throws IOException {
        return clovaSpeechProvider.recognizeFile(file);
    }

    @Override
    public String summarizeTextFromResponse(String jsonResponse) throws IOException {
        return clovaSpeechProvider.summarizeTextFromSTTResponse(jsonResponse);
    }

    @Override
    public String processAndSummarizeAudio(MultipartFile file) throws IOException {
        String sttResponse = clovaSpeechProvider.recognizeFile(file);
        return clovaSpeechProvider.summarizeTextFromSTTResponse(sttResponse);
    }
}
