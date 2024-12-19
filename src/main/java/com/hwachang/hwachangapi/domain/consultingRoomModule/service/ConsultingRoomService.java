package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.domain.Review;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ReviewRepository reviewRepository;
    private final ConsultingRoomRepository consultingRoomRepository;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;

    @Transactional
    public ConsultingRoomResponseDto createConsultingRoom(UUID customerId, UUID categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        ConsultingRoomEntity consultingRoom = ConsultingRoomEntity.builder()
                .tellerId(teller.getId())
                .customerIds(List.of(customerId))
                .categoryId(categoryId)
                .build();
        consultingRoomRepository.save(consultingRoom);

        return ConsultingRoomResponseDto.builder()
                .consultingRoom(consultingRoom.getConsultingRoomId())
                .customerId(customerId)
                .userName(customer.getUsername())
                .categoryId(categoryId)
                .build();
    }

    @Transactional
    public UUID createReview(CreateReviewDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        CustomerEntity customerEntity = this.customerRepository.findByUsername(username).orElseThrow();
        try{
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .customerId(customerEntity.getId())
                    .tellerId(dto.getTellerId())
                    .nps(dto.getNps())
                    .content(dto.getContent())
                    .consultingRoomId(dto.getConsultingRoomId())
                    .build();

            return reviewRepository.save(reviewEntity).getReviewId();

        }catch (Exception e){throw new RuntimeException("알수 없는 에러가 발생했습니다.");}
    }

}
