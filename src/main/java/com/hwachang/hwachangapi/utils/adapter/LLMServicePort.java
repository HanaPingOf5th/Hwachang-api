package com.hwachang.hwachangapi.utils.adapter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LLMServicePort {

    /**
     * 음성 파일을 텍스트로 변환
     */
    String transferAudioToText(MultipartFile file) throws IOException;

    /**
     * 음성 파일을 텍스트로 변환하고 요약 요청까지 수행
     */
    String processAndSummarizeAudio(MultipartFile file) throws IOException;
}
