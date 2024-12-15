package com.hwachang.hwachangapi.utils.adapter;

import java.io.IOException;
import java.io.InputStream;

public interface LLMServicePort {

    /**
     * 음성 파일을 텍스트로 변환
     */
    String transferAudioToText(InputStream fileStream, String fileName) throws IOException;


    /**
     * 음성 파일을 텍스트로 변환하고 요약 요청까지 수행
     */
    String processAndSummarizeAudio(InputStream fileStream, String fileName) throws IOException;
}
