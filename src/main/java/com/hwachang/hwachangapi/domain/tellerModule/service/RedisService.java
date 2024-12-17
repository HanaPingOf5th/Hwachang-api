package com.hwachang.hwachangapi.domain.tellerModule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis 키 생성
    private String generateKey(String typeId, String status) {
        return String.format("type:%s:%s", typeId, status);
    }

    // 분류 상태 저장
    public void setTypeStatus(String typeId, String status, String value) {
        String key = generateKey(typeId, status);
        redisTemplate.opsForValue().set(key, value);
    }

    // 분류 상태 조회
    public String getTypeStatus(String typeId, String status) {
        String key = generateKey(typeId, status);
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 분류 상태 삭제
    public void deleteTypeStatus(String typeId, String status) {
        String key = generateKey(typeId, status);
        redisTemplate.delete(key);
    }

    // 특정 상태의 모든 키 조회
    public Set<String> findKeyByStatus(String status) {
        String pattern = String.format("category:*:%s", status);
        return redisTemplate.keys(pattern);
    }
}
