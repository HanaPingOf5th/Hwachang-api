package com.hwachang.hwachangapi.domain.customerModule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="review_id")
    private UUID reviewId;

    @Column(name="customer_id")
    private UUID customerId;

    @Column(name="teller_id")
    private UUID tellerId;

    @Column(name="nps")
    private Integer nps;

    @Column(name="content")
    private String content;

    @Column(name="consulting_room_id")
    private UUID consultingRoomId;
}
