package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.utils.adapter.LLMServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/member/customer")
@RequiredArgsConstructor
public class LLMTestController {

    private final LLMServicePort llmServicePort;

    /**
     * 음성 Url을 텍스트로 변환하는 엔드포인트
     */
    @PostMapping("/recognize-audio")
    public ResponseEntity<String> recognizeAudio(@RequestParam String fileUrl) {
        try {
            // Object Storage에서 파일 다운로드
            InputStream inputStream = FileUtils.downloadFileAsStream(fileUrl);

            String sttResponse = llmServicePort.transferAudioToText(inputStream, fileUrl);
            return ResponseEntity.ok(sttResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing audio file: " + e.getMessage());
        }
    }

    /**
     * Object Storage URL로부터 음성 파일을 텍스트로 변환하고 요약 수행
     *
     * @param fileUrl Object Storage에 저장된 파일 URL
     * @return 요약된 텍스트
     */
    @PostMapping("/process-audio-url")
    public ResponseEntity<String> processAudioFromUrl(@RequestParam String fileUrl) {
        try {
            // Object Storage에서 파일 다운로드
            InputStream inputStream = FileUtils.downloadFileAsStream(fileUrl);

            // 음성 파일을 텍스트로 변환하고 요약 수행
            String summarizedText = llmServicePort.processAndSummarizeAudio(inputStream, fileUrl);
            return ResponseEntity.ok(summarizedText);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing audio file: " + e.getMessage());
        }
    }

}
