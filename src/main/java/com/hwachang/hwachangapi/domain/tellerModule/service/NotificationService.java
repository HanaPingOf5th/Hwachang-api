package com.hwachang.hwachangapi.domain.tellerModule.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueCustomerDto;
import com.hwachang.hwachangapi.domain.tellerModule.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository;
    private final CustomerRepository customerRepository;

    //연결 지속시간 한시간
    private static final Long DEFAULT = 60L * 1000 * 60;

    public SseEmitter subscribe(String username){
        SseEmitter emitter = createEmitter(username);

        sendToClient(username, "EventStream Created. [customerId=" + username + "]");
        return emitter;
    }

    public void notify(String username, Object event){
        sendToClient(username, event);
    }

    private void sendToClient(String username, Object data){
        SseEmitter emitter = emitterRepository.get(username);
        if (emitter == null) {
            System.out.println("Emitter not found for customerId: " + username);
            return;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(data);

            emitter.send(SseEmitter.event()
                    .id(username)
                    .name("sse")
                    .data(jsonMessage));
            System.out.println(jsonMessage);
            System.out.println("Notification sent successfully to customerId: " + username);
        } catch (IOException e) {
            emitterRepository.deleteById(username);
            emitter.completeWithError(e);
            System.err.println("Error sending notification to customerId: " + username + ", error: " + e.getMessage());
        }
    }



    private SseEmitter createEmitter(String username){
        SseEmitter emitter = new SseEmitter(DEFAULT);
        emitterRepository.save(username, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(username));
        emitter.onTimeout(() -> emitterRepository.deleteById(username));
        emitter.onError((e) -> emitterRepository.deleteById(username));

        return emitter;
    }
}
