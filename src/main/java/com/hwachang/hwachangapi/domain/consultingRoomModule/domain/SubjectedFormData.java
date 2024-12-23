package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.*;

import java.io.Serializable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectedFormData implements Serializable {
    String section;
    String label;
    String value;
}
