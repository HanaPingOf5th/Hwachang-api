package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ConsultingRoom {
    private UUID consultingRoomId;

    private UUID tellerId;

    private UUID categoryId;

    private List<UUID> customerIds;

    private List<Map<String, Object>> originalText;

    private String summary; // 요약을 String 형태로 저장

    private List<String> recordChat;

    private String voiceRecordUrl;

    private String time;

    public static ConsultingRoom create(UUID consultingRoomId, UUID tellerId, UUID categoryId, List<UUID> customerIds, List<Map<String, Object>> originalText, String summary, List<String> recordChat, String voiceRecordUrl, String time){
        return new ConsultingRoom(consultingRoomId, tellerId, categoryId, customerIds, originalText, summary, recordChat, voiceRecordUrl, time);
    }
}
