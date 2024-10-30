package com.hwachang.hwachangapi.controller;

import com.hwachang.hwachangapi.service.ClovaSpeechClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


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
    @PostMapping("/summarize")
    public ResponseEntity<String> summarizeText(@RequestBody String jsonResponse) {
        try {
            String summary = clovaSpeechClientService.summarizeText(jsonResponse);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error summarizing text: " + e.getMessage());
        }
    }

}
