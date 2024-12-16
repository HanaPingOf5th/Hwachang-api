package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.domain.Review;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ConsultingRoomRepository consultingRoomRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public UUID createReview(CreateReviewDto dto) {
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .customerId(dto.getCustomerId())
                .nps(dto.getNps())
                .content(dto.getContent())
                .consultingRoomId(dto.getConsultingRoomId())
                .build();

        return reviewRepository.save(reviewEntity).getReviewId();
    }

}
