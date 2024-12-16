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
        String sttResponse = clovaSpeechProvider.recognizeFile(fileStream, fileName);
        return clovaSpeechProvider.summarizeTextFromSTTResponse(sttResponse);
    }
}
