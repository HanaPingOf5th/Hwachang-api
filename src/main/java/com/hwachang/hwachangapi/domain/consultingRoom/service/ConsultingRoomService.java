package com.hwachang.hwachangapi.domain.consultingRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwachang.hwachangapi.domain.consultingRoom.entity.ChatRecord;
import com.hwachang.hwachangapi.domain.consultingRoom.entity.ConsultingRoom;
import com.hwachang.hwachangapi.domain.consultingRoom.repository.ConsultingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ConsultingRoomRepository consultingRoomRepository;
    private final ObjectMapper objectMapper;

    public void updateChatRecord(String roomId, String message) {
        ChatRecord chatRecord = objectMapper.convertValue(message, ChatRecord.class);

        ConsultingRoom consultingRoom = consultingRoomRepository.findById(roomId).orElseGet(() -> new ConsultingRoom(roomId));
        if(consultingRoom.getChatRecord() == null) {
            consultingRoom.setChatRecord(new ChatRecord(chatRecord.getUserId(), chatRecord.getMessage(), chatRecord.getTime()));
        }
        else{
            consultingRoom.getChatRecord().add(chatRecord);
        }
        consultingRoomRepository.save(consultingRoom);
    }
}
