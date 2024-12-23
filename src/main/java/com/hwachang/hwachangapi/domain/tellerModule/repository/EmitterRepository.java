package com.hwachang.hwachangapi.domain.tellerModule.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(UUID customerId, SseEmitter emitter) {
        emitters.put(customerId, emitter);
    }

    public void deleteById(UUID customerId) {
        emitters.remove(customerId);
    }

    public SseEmitter get(UUID customerId) {
        return emitters.get(customerId);
    }
}
