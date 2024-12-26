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

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    public SseEmitter get(String emitterId) {
        return emitters.get(emitterId);
    }
}
