package com.hwachang.hwachangapi.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NestRequestEntity {
    private String language = "ko-KR"; // 기본 언어 설정
    private String completion = "sync"; // 동기/비동기 방식
    private Boolean fullText = true; // 전체 텍스트 반환 여부
    private String format = "JSON"; // 반환 형식 (JSON)

    // 선택 필드 (필요시 초기화하여 사용할 수 있도록 설정)
    private String callback; // 비동기 요청 시 콜백 URL
    private Boolean wordAlignment; // 음성-텍스트 정렬
    private Boolean noiseFiltering; // 노이즈 필터링 여부
    private List<String> boostings; // 부스팅할 키워드 목록
    private String forbiddens; // 민감 키워드

    private Diarization diarization = new Diarization(false); // 기본값으로 false로 초기화
    private SED sed = new SED(false); // 기본값으로 false로 초기화

    // 생성자 - 필수 필드 초기화 (언어, 방식, 전체 텍스트, 형식)
    public NestRequestEntity() {
        this.language = "ko-KR";
        this.completion = "sync";
        this.fullText = true;
        this.format = "JSON";
    }
}
