package com.hwachang.hwachangapi.domain.tellerModule.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueCustomerDto;
import com.hwachang.hwachangapi.domain.tellerModule.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
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

    public SseEmitter subscribe(UUID customerId){
        SseEmitter emitter = createEmitter(customerId);
        emitter.onCompletion(() -> System.out.println("SSE connection completed for customer: " + customerId));
        emitter.onTimeout(() -> System.out.println("SSE connection timed out for customer: " + customerId));

        sendToClient(customerId, "EventStream Created. [customerId=" + customerId + "]");
        return emitter;
    }

    public void notify(UUID customerId, Object event){
        sendToClient(customerId, event);
    }

    private void sendToClient(UUID customerId, Object data){
        SseEmitter emitter = emitterRepository.get(customerId);
        if(emitter != null){
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(data);

                emitter.send(SseEmitter.event()
                        .id(String.valueOf(customerId))
                        .name("sse")
                        .data(jsonMessage));
                System.out.println("성공!");
            }catch (IOException e){
                emitterRepository.deleteById(customerId);
                emitter.completeWithError(e);
            }
        }
    }


    private SseEmitter createEmitter(UUID customerId){
        SseEmitter emitter = new SseEmitter(DEFAULT);
        emitterRepository.save(customerId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(customerId));

        emitter.onTimeout(() -> emitterRepository.deleteById(customerId));

        return emitter;
    }
}
