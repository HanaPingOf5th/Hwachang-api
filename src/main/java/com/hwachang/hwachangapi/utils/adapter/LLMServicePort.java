package com.hwachang.hwachangapi.utils.adapter;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface LLMServicePort {

    /**
     * 음성 파일을 텍스트로 변환
     */
    List<Map<String, Object>> transferAudioToText(InputStream fileStream, String fileName) throws IOException;


    /**
     * 음성 파일을 텍스트로 변환하고 요약 요청까지 수행
     */
    String processAndSummarizeAudio(InputStream fileStream, String fileName) throws IOException;
}
