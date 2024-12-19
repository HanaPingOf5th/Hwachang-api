package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueCustomerDto;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.TypeHandler;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Log4j2
@RequiredArgsConstructor
public class WaitingQueueService {
    private final RedisTemplate<String, PriorityQueue<QueueCustomerDto>> redisTemplate;
    private final CustomerRepository customerRepository;
    private static final String WAIT_QUEUE_KEY = "waiting_queue:";
    private AtomicLong counter = new AtomicLong(0);

    // 고객 대기열 입장 - 고객
    public boolean addCustomerToQueue(int typeId, UUID categoryId, String userName) {
        String key = getQueueKey(typeId);

        CustomerEntity customer = customerRepository.findByUsername(userName)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Long waitingNumber = counter.incrementAndGet();

        QueueCustomerDto customerDto = QueueCustomerDto.builder()
                .customerId(customer.getId())
                .userName(userName)
                .categoryId(categoryId)
                .waitingNumber(waitingNumber)
                .build();

        // Redis에서 우선순위 큐 가져오기
        PriorityQueue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        if (queue == null) {
            queue = new PriorityQueue<>();
        }

        // 동일한 고객이 있는지 확인
        boolean isDuplicate = queue.stream()
                .anyMatch(dto -> dto.getUserName().equals(customerDto.getUserName()));
        if (isDuplicate) {
            log.warn("이미 큐에 추가된 고객입니다: {}", userName);
            return false;
        }

        // 우선순위 큐에 고객 추가
        queue.add(customerDto);

        // 변경된 큐를 Redis에 저장
        redisTemplate.opsForValue().set(key, queue);
        log.info("{}: {} 고객 추가 (번호표 {})", key, userName, waitingNumber);
        return true;
    }

    // 다음 고객 처리 - 행원
    public QueueCustomerDto processNextCustomer(int typeId) {
        String key = getQueueKey(typeId);

        // Redis에서 우선순위 큐 가져오기
        PriorityQueue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        if (queue == null || queue.isEmpty()) {
            log.warn("{}: 큐가 없거나 비어 있습니다.", key);
            return null;
        }

        // 우선순위 큐에서 가장 높은 우선순위의 고객 제거
        QueueCustomerDto nextCustomer = queue.poll();

        // 변경된 큐를 Redis에 저장
        redisTemplate.opsForValue().set(key, queue);
        log.info("대기열에서 고객 처리: {}", nextCustomer.getUserName());

        return nextCustomer;
    }

    // 고객 대기열 입장 취소 - 고객
    public boolean removeCustomerFromQueue(String userName, int typeId) {
        String key = getQueueKey(typeId);

        // Redis에서 우선순위 큐 가져오기
        PriorityQueue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        if (queue == null || queue.isEmpty()) {
            log.warn("{}: 큐가 없거나 비어 있습니다.", key);
            return false;
        }

        // 해당 고객 제거
        boolean removed = queue.removeIf(dto -> dto.getUserName().equals(userName));

        // 변경된 큐를 Redis에 저장
        redisTemplate.opsForValue().set(key, queue);
        log.info("{}: {} 고객 퇴장: {}", key, userName, removed);

        return removed;
    }

    // 대기열 크기 반환
    public int getWaitingQueueSize(int typeId) {
        String key = getQueueKey(typeId);

        PriorityQueue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        log.info("큐의 크기: {}", (queue != null) ? queue.size() : 0);

        return (queue != null) ? queue.size() : 0;
    }

    // 고유한 Redis 키 생성
    private String getQueueKey(int typeId) {
        String suffix = (typeId == 0) ? "personal" :
                (typeId == 1) ? "corporate" : null;
        if (suffix == null) {
            throw new TypeHandler(ErrorStatus.INVALID_TYPE);
        }
        return WAIT_QUEUE_KEY + suffix;
    }
}