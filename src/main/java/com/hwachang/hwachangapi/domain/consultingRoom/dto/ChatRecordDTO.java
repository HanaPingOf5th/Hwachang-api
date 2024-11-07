package com.hwachang.hwachangapi.domain.consultingRoom.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class ChatRecordDTO {

    private String userId;
    private String message;
    private LocalDateTime time;
}
