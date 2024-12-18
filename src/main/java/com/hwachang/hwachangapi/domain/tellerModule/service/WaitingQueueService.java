package com.hwachang.hwachangapi.domain.tellerModule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    private static final String WAIT_QUEUE_KEY = "waiting_queue:";

    public void initializeZSetOperations() {
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    // 대기열에 고객 추가
    public void addCustomerToQueue(String customerId, String typeId) {
        initializeZSetOperations();
        String key = getQueueKey(typeId);
        // 대기 순서를 정하기 위해 현재 시간을 timestamp로 사용
        long timestamp = System.currentTimeMillis();

        // ZSet에 고객을 추가, timestamp를 score로 설정하여 대기 순서 관리
        zSetOperations.add(key, customerId, timestamp);
    }

    // 대기열에서 고객을 불러와 입장 처리
    public String processNextCustomer(String typeId) {
        initializeZSetOperations();
        String key = getQueueKey(typeId);

        // ZSet에서 가장 오래된 고객을 가져옴 (score가 가장 작은 고객)
        Set<ZSetOperations.TypedTuple<String>> tokenSet = zSetOperations.rangeWithScores(key, 0, 0);

        // 대기열이 비어 있는 경우
        if (tokenSet == null || tokenSet.isEmpty()) {
            return null;
        }

        // 가장 오래된 고객을 고객 대기열에서 제거
        String customerId = tokenSet.iterator().next().getValue();
        zSetOperations.remove(key, customerId);

        return customerId;
    }

    // 대기열에서 고객 제거
    public boolean removeCustomerFromQueue(String customerId, String typeId) {
        initializeZSetOperations();
        String key = getQueueKey(typeId);

        Long removedCount = zSetOperations.remove(key, customerId);

        // 제거된 고객이 있으면 removedCount가 1 이상, 없으면 0
        return removedCount > 0;
    }

    // 대기열에 있는 고객 수 반환
    public Long getWaitingQueueSize(String typeId) {
        initializeZSetOperations();
        String key = getQueueKey(typeId);
        return zSetOperations.zCard(key);
    }

    // 고유한 Redis 키 생성
    private String getQueueKey(String typeId) {
        return WAIT_QUEUE_KEY + typeId;
    }
}