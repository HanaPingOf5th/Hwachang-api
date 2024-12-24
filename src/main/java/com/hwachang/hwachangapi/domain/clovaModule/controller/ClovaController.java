package com.hwachang.hwachangapi.domain.clovaModule.controller;

import com.google.gson.JsonObject;
import com.hwachang.hwachangapi.domain.clovaModule.service.ClovaApiService;
import com.hwachang.hwachangapi.domain.clovaModule.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class ClovaController {

    private final ClovaApiService clovaApiService;
    private final FileUploadService fileUploadService;

    /**
     * 음성파일 업로드 후 URL 반환
     */
    // Todo : 이 부분은 프론트로 옮길 예정
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileUploadService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("recognize-all")
    public ResponseEntity<String> recognizeAll(@RequestParam String fileUrl) {
        try{
            String result = clovaApiService.recognizeAll(fileUrl);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 음성 파일 URL을 텍스트로 변환
     */
    @PostMapping("/recognize-audio")
    public ResponseEntity<List<Map<String, Object>>> recognizeAudio(@RequestParam String fileUrl) {
        try {
            List<Map<String, Object>> result = clovaApiService.recognizeAudio(fileUrl);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
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
