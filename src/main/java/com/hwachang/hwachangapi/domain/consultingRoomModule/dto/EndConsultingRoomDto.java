package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class EndConsultingRoomDto {
    private UUID consultingRoomId;
    private UUID categoryId;
    private List<UUID> customerIds;
    private List<String> recordChat;
    private UUID tellerId;
    private String time;
    private String voiceUrl;
}

/*
{
    "consultingRoomId":"1e034bdd-25b7-4e37-b940-cbc86256882a",
    "categoryId":"03f4aa15-56eb-487f-aa7b-be49230e7b97",
    "customerIds":["31bb2d97-24be-4f80-8178-5d7c95f68a6a"],
    "recordChat": [],
    "tellerId": "0acf3f0a-8e12-4ce4-a2f9-3c2842a4256b",
    "time":"2024-12-27T02:14:22.870Z",
    "voiceUrl":"https://kr.object.ncloudstorage.com/consulting-data-1e034bdd-25b7-4e37-b940-cbc86256882a.mp4"
}
 */
