package com.hwachang.hwachangapi.tellerModule;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.LogData;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.JpaTellerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerConsultingRoomRepository;
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
    private TellerConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private TellerRepository tellerRepository;


    @Test
    public void testInsertConsultingRoom() {
        TellerEntity teller = tellerRepository.findTellerByUserName("hana_0005").orElseThrow();
        for (int i = 0; i < 10; i++) {
            ConsultingRoomEntity consultingRoom = ConsultingRoomEntity.builder()
                    .bankerId(teller.getId())
                    .categoryId(teller.getId())
                    .recordChat(List.of("Chat1", "Chat2"))
                    .time("Test Time")
                    .title("Test Title")
                    .voiceRecord("Test Voice")
                    .summary("Test Summary")
                    .build();
            consultingRoomRepository.save(consultingRoom);
        }
    }

    @Test
    public void testRead() {
        TellerEntity teller = tellerRepository.findTellerByUserName("hana_0005").orElseThrow();
        LogData graphData = service.readGraphData(teller);

        log.info("응답 데이터");
        log.info("응답 일별 : {}", graphData.getDailyLog().today);
        log.info("응답 주별 : {}", graphData.getWeeklyLog().thisWeek);
        log.info("응답 월별 : {}", graphData.getMonthlyLog().thisMonth);
    }

}
