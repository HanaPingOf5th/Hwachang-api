//package com.hwachang.hwachangapi.domain.tellerModule.repository;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class JpaEmitterRepository implements EmitterRepository{
//    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final Map<UUID, Object> eventCache = new ConcurrentHashMap<>();
//
//    @Override
//    public void save(UUID emitterId, SseEmitter sseEmitter){
//        emitters.put(emitterId, sseEmitter);
//    }
//
//    @Override
//    public void deleteById(UUID customerId) {
//        emitters.remove(customerId);
//    }
//
//    @Override
//    public SseEmitter get(UUID customerId) {
//        return emitters.get(customerId);
//    }
//}
