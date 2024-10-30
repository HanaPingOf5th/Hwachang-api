package com.hwachang.hwachangapi.controller;

import com.hwachang.hwachangapi.service.ClovaSpeechClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/clova-speech")
@RequiredArgsConstructor
public class ClovaSpeechController {

    private final ClovaSpeechClientService clovaSpeechClientService;

    @PostMapping("/recognize")
    public ResponseEntity<String> recognizeSpeech(@RequestPart("file") MultipartFile file){
        try{
            String result = clovaSpeechClientService.recognizeFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}
