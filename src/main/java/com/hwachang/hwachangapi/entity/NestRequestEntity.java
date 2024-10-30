package com.hwachang.hwachangapi.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter @Setter
public class NestRequestEntity {
    private String language = "ko-KR"; //기본 언어 설정
    private String completion = "sync";
    private String callback; // 결과 수신을 위한 콜백 URL
    private Boolean wordAlignment = Boolean.TRUE;
    private Boolean fullText = Boolean.TRUE;
    private List<Boosting> boostings;
    private String forbiddens;
    private Diarization diarization;
    private Map<String, Object> userdata;
}
