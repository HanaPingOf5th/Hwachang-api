package com.hwachang.hwachangapi.tellerModule;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.JpaTellerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.service.HwaChangLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class HwaChangLogServiceTests {

    private static final Logger log = LogManager.getLogger(HwaChangLogServiceTests.class);
    @Autowired
    private HwaChangLogService service;

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private TellerRepository tellerRepository;


    @Test
    public void testInsertConsultingRoom() {
        for (int i = 0; i < 10; i++) {
            ConsultingRoomEntity consultingRoom = ConsultingRoomEntity.builder()
                    .bankerId(UUID.fromString("575ed844-58c0-4170-8dab-e4544c7fe16a"))
                    .categoryId(UUID.fromString("575ed844-58c0-4170-8dab-e4544c7fe16a"))
                    .originalText("Test Text")
                    .recordChat(List.of("Chat1", "Chat2"))
                    .title("Test Title")
                    .time("Test Time")
                    .voiceRecord("Test Voice")
                    .build();
            consultingRoomRepository.save(consultingRoom);
        }
    }

    @Test
    public void testRead() {
        TellerEntity teller = tellerRepository.findById(UUID.fromString("575ed844-58c0-4170-8dab-e4544c7fe16a")).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        log.info("응답 데이터 : {}", service.readGraphData(teller, now));
    }

}
