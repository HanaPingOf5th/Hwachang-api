package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.utils.adapter.LLMServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class LLMTestController {

    private final LLMServicePort llmServicePort;

    /**
     * 음성 파일을 텍스트로 변환하는 엔드포인트
     *
     * @param file 업로드된 음성 파일
     * @return 변환된 텍스트
     */
    @PostMapping("/recognize-audio")
    public ResponseEntity<String> recognizeAudio(@RequestPart MultipartFile file) {
        try {
            String sttResponse = llmServicePort.transferAudioToText(file);
            return ResponseEntity.ok(sttResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing audio file: " + e.getMessage());
        }
    }

    /**
     * 음성 파일을 텍스트로 변환하고 요약 요청까지 수행하는 엔드포인트
     *
     * @param file 업로드된 음성 파일
     * @return 요약된 텍스트
     */
    @PostMapping("/process-audio")
    public ResponseEntity<String> processAudio(@RequestPart MultipartFile file) {
        try {
            // 음성 파일을 텍스트로 변환하고 요약 수행
            String summarizedText = llmServicePort.processAndSummarizeAudio(file);
            return ResponseEntity.ok(summarizedText);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing and summarizing audio file: " + e.getMessage());
        }
    }
}
