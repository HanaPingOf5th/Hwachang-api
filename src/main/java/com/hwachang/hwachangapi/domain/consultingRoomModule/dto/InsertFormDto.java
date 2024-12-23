package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.SubjectedFormData;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsertFormDto implements Serializable {
    List<SubjectedFormData> subjectedFormData;
}
