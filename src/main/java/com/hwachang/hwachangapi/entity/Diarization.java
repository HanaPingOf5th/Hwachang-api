package com.hwachang.hwachangapi.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Diarization {

    private Boolean enable = Boolean.TRUE; //화자 분리 활성화 여부
    private Integer speakerCountMin; // 최소 화자 수
    private Integer speakerCountMax; // 최대 화자 수


}
