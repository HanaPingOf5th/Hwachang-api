package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueCustomerDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Status;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.TypeHandler;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class WaitingQueueService {
    private final RedisTemplate<String, ConsultingRoomResponseDto> consultingRoomRedisTemplate;
    private final RedisTemplate<String, Queue<QueueCustomerDto>> redisTemplate;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private static final String WAIT_QUEUE_KEY = "waiting_queue:";
    private static final String CONSULTING_KEY = ":consulting";
    private AtomicLong personalCounter = new AtomicLong(0);
    private AtomicLong corporateCounter = new AtomicLong(0);

    // 고객 대기열 입장 - 고객
    public String addCustomerToQueue(int typeId, UUID categoryId, String userName) {
        String key = getQueueKey(typeId);

        CustomerEntity customer = customerRepository.findByUsername(userName)
                .orElseThrow(() -> new UserHandler(ErrorStatus.CUSTOMER_NOT_FOUND));

        Long waitingNumber = typeId == 0 ? personalCounter.incrementAndGet() : corporateCounter.incrementAndGet();

        QueueCustomerDto customerDto = QueueCustomerDto.builder()
                .customerId(customer.getId())
                .userName(userName)
                .categoryId(categoryId)
                .waitingNumber(waitingNumber)
                .build();

        // Redis에서 큐 가져오기
        Queue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        if (queue == null) {
            queue = new PriorityQueue<>();
        }

        // 동일한 고객이 있는지 확인
        boolean isDuplicate = queue.stream()
                .anyMatch(dto -> dto.getUserName().equals(customerDto.getUserName()));
        if (isDuplicate) {
            log.warn("이미 큐에 추가된 고객입니다: {}", userName);
            return null;
        }

        // 큐에 고객 추가
        queue.add(customerDto);

        // 변경된 큐를 Redis에 저장
        Long expiredTime = 600L;
        redisTemplate.opsForValue().set(key, queue, expiredTime, TimeUnit.SECONDS);
        log.info("{}: {} 고객 추가 (번호표 {})", key, userName, waitingNumber);
        return customer.getName();
    }

    // 다음 고객 처리 - 행원
    public QueueCustomerDto processNextCustomer(int typeId) {
        String key = getQueueKey(typeId);

        // Redis에 큐 가져오기
        Queue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        if (queue == null || queue.isEmpty()) {
            log.warn("{}: 큐가 없거나 비어 있습니다.", key);
            return null;
        }

        // 큐에서 가장 높은 우선순위의 고객 제거
        QueueCustomerDto nextCustomer = queue.poll();

        // 변경된 큐를 Redis에 저장
        redisTemplate.opsForValue().set(key, queue);
        log.info("대기열에서 고객 처리: {}", nextCustomer.getUserName());

        return nextCustomer;
    }

    // 고객 대기열 입장 취소 - 고객
    public boolean removeCustomerFromQueue(String userName, int typeId) {
        String key = getQueueKey(typeId);

        // Redis에서 큐 가져오기
        Queue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
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

    // 고객에게 제공할 상담실 정보 생성
    public String createConsultingRoomInfo(UUID customerId, ConsultingRoomResponseDto dto) {
        String key = getConsultingQueueKey(customerId);
        Long expiredTime = 600L;
        consultingRoomRedisTemplate.opsForValue().set(key, dto, expiredTime, TimeUnit.SECONDS);
        log.info("{}: {}", key, dto);
        return key;
    }

    // 고객이 필요한 정보
    public ConsultingRoomResponseDto getConsultingRoomInfo(String userName) {
        CustomerEntity customerEntity = customerRepository.findByUsername(userName)
                .orElseThrow(() -> new UserHandler(ErrorStatus.CUSTOMER_NOT_FOUND));

        UUID customerId = customerEntity.getId();
        String key = customerId + CONSULTING_KEY;

        ValueOperations<String, ConsultingRoomResponseDto> valueOperations = consultingRoomRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // 대기열 크기 반환
    public Long getWaitingQueueSize(int typeId) {
        String key = getQueueKey(typeId);
        log.info("KEY: {}, TYPEID: {}", key, typeId);
        Queue<QueueCustomerDto> queue = redisTemplate.opsForValue().get(key);
        log.info("큐의 크기: {}", (queue != null) ? queue.size() : 0L);

        return (queue != null) ? (long) queue.size() : 0L;
    }

    public QueueResponseDto getWaitingQueuesInfo(int typeId) {
        long waitingTeller = getTellerCountByStatus(Status.AVAILABLE);
        long calling = getTellerCountByStatus(Status.UNAVAILABLE);
        long postProcessing = getTellerCountByStatus(Status.BUSY);

        return QueueResponseDto.builder()
                .waitingCustomer(getWaitingQueueSize(typeId))
                .waitingTeller(waitingTeller)
                .calling(calling)
                .postProcessing(postProcessing)
                .build();
    }

    public long getTellerCountByStatus(Status status) {
        return tellerRepository.countByStatus(status);
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

    private String getConsultingQueueKey(UUID customerId) {
        return customerId + CONSULTING_KEY;
    }
}