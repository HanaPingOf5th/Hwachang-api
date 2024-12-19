package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class QueueCustomerDto implements Comparable<QueueCustomerDto> {
    private UUID customerId;
    private String userName;
    private Long waitingNumber;
    private UUID categoryId;

    @Override
    public int compareTo(QueueCustomerDto other) {
        return Long.compare(this.waitingNumber, other.waitingNumber);
    }

    @JsonCreator
    public QueueCustomerDto(@JsonProperty("customerId") UUID customerId,
                            @JsonProperty("customerName") String customerName,
                            @JsonProperty("waitingNumber") Long waitingNumber,
                            @JsonProperty("categoryId") UUID categoryId) {
        this.customerId = customerId;
        this.userName = customerName;
        this.waitingNumber = waitingNumber;
        this.categoryId = categoryId;
    }
}

