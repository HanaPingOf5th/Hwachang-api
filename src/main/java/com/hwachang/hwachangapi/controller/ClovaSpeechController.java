package com.hwachang.hwachangapi.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hwachang.hwachangapi.service.ClovaSpeechClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @PostMapping("/upload")
    public ResponseEntity<List<Map<String, String>>> uploadAndProcessFile(@RequestPart("file") MultipartFile file) {
        try {
            // 음성 파일을 JSON으로 변환
            String jsonResponse = clovaSpeechClientService.recognizeFile(file);

            // JSON을 파싱하여 텍스트와 시간대 데이터 추출
            List<Map<String, String>> parsedSegments = parseJsonResponse(jsonResponse);

            // 원본 음성을 분리하여 구간별 오디오 파일 생성
            String audioFilePath = "/Users/kim-in-young/Desktop/test-audio-file/" + file.getOriginalFilename(); // 원본 경로
            file.transferTo(new File(audioFilePath)); // 업로드된 파일 저장
            splitAudioBySegments(audioFilePath, parsedSegments);

            return ResponseEntity.ok(parsedSegments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of(Map.of("error", e.getMessage())));
        }
    }

    @PostMapping("/summarize-file")
    public ResponseEntity<Map<String, String>> summarizeFile(@RequestPart("file") MultipartFile file) {
        try {
            // 1. 음성 파일을 텍스트로 변환
            String jsonResponse = clovaSpeechClientService.recognizeFile(file);

            // 2. 변환된 텍스트를 요약
            String summary = clovaSpeechClientService.summarizeText(jsonResponse);

            // 3. 결과를 Map으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("summary", summary); // 요약된 텍스트 반환

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 4. 에러 처리
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/parse-json")
    public ResponseEntity<List<Map<String, String>>> parseSpeechJson(@RequestBody String jsonResponse) {
        try {
            // 파싱 로직 호출
            List<Map<String, String>> result = parseJsonResponse(jsonResponse);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of(Map.of("error", e.getMessage())));
        }
    }
    private List<Map<String, String>> parseJsonResponse(String jsonResponse) {
        List<Map<String, String>> parsedSegments = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray segments = jsonObject.getAsJsonArray("segments");

        for (int i = 0; i < segments.size(); i++) {
            JsonObject segment = segments.get(i).getAsJsonObject();

            // 시작 시간, 종료 시간, 텍스트, 발화자 추출
            int start = segment.get("start").getAsInt();
            int end = segment.get("end").getAsInt();
            String text = segment.get("text").getAsString();
            String speaker = segment.get("diarization").getAsJsonObject().get("label").getAsString();

            // 결과를 Map으로 저장
            Map<String, String> segmentData = new HashMap<>();
            segmentData.put("speaker", "Speaker " + speaker);
            segmentData.put("startTime", convertMsToTimeFormat(start));
            segmentData.put("endTime", convertMsToTimeFormat(end));
            segmentData.put("text", text);
            segmentData.put("audioUrl", "/audio/segment_" + i + ".mp3"); // 오디오 파일 경로

            parsedSegments.add(segmentData);
        }

        return parsedSegments;
    }

    // 음성 파일 분리
    private void splitAudioBySegments(String audioFilePath, List<Map<String, String>> segments) throws IOException {
        File inputFile = new File(audioFilePath);
        if (!inputFile.exists()) {
            throw new IOException("Input file not found: " + audioFilePath);
        }

        for (int i = 0; i < segments.size(); i++) {
            Map<String, String> segment = segments.get(i);
            String startTime = segment.get("startTime");
            String endTime = segment.get("endTime");
            String outputPath = String.format("/Users/kim-in-young/Desktop/test-audio-file/segment_%d.m4a", i);

            // 명령어 생성
            String command = String.format("ffmpeg -y -i %s -ss %s -to %s -c copy %s",
                    audioFilePath, startTime, endTime, outputPath);

            System.out.println("Executing command: " + command);

            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = stdOut.readLine()) != null) {
                    System.out.println("FFmpeg Output: " + line);
                }
                while ((line = stdErr.readLine()) != null) {
                    System.err.println("FFmpeg Error: " + line);
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new IOException("FFmpeg command failed with exit code " + exitCode);
                }
            } catch (InterruptedException e) {
                throw new IOException("FFmpeg process was interrupted", e);
            }
        }
    }


    private String convertMsToTimeFormat(int ms) {
        int seconds = ms / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
