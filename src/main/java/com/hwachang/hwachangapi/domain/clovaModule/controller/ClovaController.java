package com.hwachang.hwachangapi.domain.clovaModule.controller;

import com.hwachang.hwachangapi.domain.clovaModule.service.ClovaApiService;
import com.hwachang.hwachangapi.domain.clovaModule.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class ClovaController {

    private final ClovaApiService clovaApiService;
    private final FileUploadService fileUploadService;

    /**
     * 파일 업로드 후 URL 반환
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileUploadService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    /**
     * 음성 파일 URL을 텍스트로 변환
     */
    @PostMapping("/recognize-audio")
    public ResponseEntity<String> recognizeAudio(@RequestParam String fileUrl) {
        try {
            String result = clovaApiService.recognizeAudio(fileUrl);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Audio recognition failed: " + e.getMessage());
        }
    }

    /**
     * 음성 파일 URL을 텍스트로 변환 후 요약
     */
    @PostMapping("/process-audio-url")
    public ResponseEntity<String> processAndSummarizeAudio(@RequestParam String fileUrl) {
        try {
            String result = clovaApiService.processAndSummarizeAudio(fileUrl);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Audio processing failed: " + e.getMessage());
        }
    }
}
