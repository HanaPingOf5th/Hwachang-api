package com.hwachang.hwachangapi.domain.customerModule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Review {
    private UUID reviewId;

    private UUID customerId;

    private Integer nps;

    private String content;

    private UUID consultingRoomId;

    public static Review create(UUID reviewId, UUID customerId, Integer nps, String content, UUID consultingRoomId) {
        return new Review(reviewId, customerId, nps, content, consultingRoomId);
    }
}
