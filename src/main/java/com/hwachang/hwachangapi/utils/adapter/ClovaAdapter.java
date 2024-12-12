package com.hwachang.hwachangapi.utils.adapter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ClovaAdapter implements LLMServicePort {

    private final ClovaSpeechService clovaSpeechService;

    public ClovaAdapter(ClovaSpeechService clovaSpeechService) {
        this.clovaSpeechService = clovaSpeechService;
    }

    @Override
    public String transferAudioToText(MultipartFile file) throws IOException {
        return clovaSpeechService.recognizeFile(file);
    }

    @Override
    public String summarizeTextFromResponse(String jsonResponse) throws IOException {
        return clovaSpeechService.summarizeTextFromSTTResponse(jsonResponse);
    }

    @Override
    public String processAndSummarizeAudio(MultipartFile file) throws IOException {
        String sttResponse = clovaSpeechService.recognizeFile(file);
        return clovaSpeechService.summarizeTextFromSTTResponse(sttResponse);
    }
}
