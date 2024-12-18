package com.hwachang.hwachangapi.tellerModule;

import com.hwachang.hwachangapi.domain.tellerModule.service.WaitingQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WaitingQueueServiceTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private WaitingQueueService waitingQueueService;

    @BeforeEach
    void setUp() {
        waitingQueueService = new WaitingQueueService(redisTemplate);
        waitingQueueService.initializeZSetOperations();
    }

    @Test
    void testAddCustomerToQueue() {
        String customerId = "customer1";
        String typeId = "type1";

        waitingQueueService.addCustomerToQueue(customerId, typeId);

        Long queueSize = waitingQueueService.getWaitingQueueSize(typeId);
        assertThat(queueSize).isEqualTo(1L);
    }

    @Test
    void testProcessNextCustomer() {
        String typeId = "type1";

        waitingQueueService.addCustomerToQueue("customer1", typeId);
        waitingQueueService.addCustomerToQueue("customer2", typeId);

        String nextCustomer = waitingQueueService.processNextCustomer(typeId);
        assertThat(nextCustomer).isEqualTo("customer1");

        Long queueSize = waitingQueueService.getWaitingQueueSize(typeId);
        assertThat(queueSize).isEqualTo(1L);
    }

    @Test
    void testGetWaitingQueueSize() {
        String typeId = "type1";

        waitingQueueService.addCustomerToQueue("customer1", typeId);
        waitingQueueService.addCustomerToQueue("customer2", typeId);

        Long queueSize = waitingQueueService.getWaitingQueueSize(typeId);
        assertThat(queueSize).isEqualTo(2L);
    }
}