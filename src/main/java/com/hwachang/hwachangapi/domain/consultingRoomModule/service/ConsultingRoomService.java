package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.clovaModule.utils.FileUtils;
import com.hwachang.hwachangapi.domain.consultingRoomModule.controller.ConsultingRoomController;
import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ConsultingRoom;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.ConsultingListDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;

import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.JpaConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import com.hwachang.hwachangapi.utils.adapter.LLMServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ReviewRepository reviewRepository;
    private final JpaConsultingRoomRepository consultingRoomRepository;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final LLMServicePort llmServicePort;
    private final CategoryRepository categoryRepository;

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

    @Transactional
    public UUID updateConsultingRoomDetails(UUID consultingRoomId, UUID tellerId, UUID categoryId, List<UUID> customerIds, List<String> recordChat, String voiceUrl, String time) {
        try {
            // 오디오 파일 다운로드 및 처리
            InputStream inputStream = FileUtils.downloadFileAsStream(voiceUrl);
            byte[] audioBytes = inputStream.readAllBytes();

            InputStream inputStreamForText = new ByteArrayInputStream(audioBytes);
            InputStream inputStreamForSummary = new ByteArrayInputStream(audioBytes);

            // 오디오 처리 및 요약 생성
            List<Map<String, Object>> originalText = llmServicePort.transferAudioToText(inputStreamForText, voiceUrl);
            String summary = llmServicePort.processAndSummarizeAudio(inputStreamForSummary, voiceUrl);

            // Consulting Room Entity 조회
            ConsultingRoomEntity consultingRoomEntity = consultingRoomRepository.findById(consultingRoomId).orElseThrow();

            // ConsultingRoom 도메인 객체 생성 및 업데이트
            ConsultingRoom updatedConsultingRoom = ConsultingRoom.create(
                    consultingRoomEntity.getConsultingRoomId(),
                    tellerId,
                    categoryId,
                    customerIds,
                    originalText,
                    summary,
                    recordChat,
                    voiceUrl,
                    time
            );

            consultingRoomEntity.updateConsultingRoomDetails(
                    ConsultingRoomEntity.builder()
                            .consultingRoomId(updatedConsultingRoom.getConsultingRoomId())
                            .tellerId(updatedConsultingRoom.getTellerId())
                            .categoryId(updatedConsultingRoom.getCategoryId())
                            .customerIds(updatedConsultingRoom.getCustomerIds())
                            .originalText(updatedConsultingRoom.getOriginalText())
                            .summary(updatedConsultingRoom.getSummary())
                            .recordChat(updatedConsultingRoom.getRecordChat())
                            .voiceRecordUrl(updatedConsultingRoom.getVoiceRecordUrl())
                            .time(updatedConsultingRoom.getTime())
                            .build()
            );

            return consultingRoomEntity.getConsultingRoomId();

        } catch (IOException e) {
            throw new RuntimeException("오디오 처리 중 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("예상치 못한 오류 발생", e);
        }
    }
    @Transactional
    public List<ConsultingListDto> getConsultingList(UUID customerId) {
        List<ConsultingRoomEntity> allConsultingRooms = consultingRoomRepository.findAll();
        System.out.println(allConsultingRooms.get(0).getConsultingRoomId());
        List<ConsultingRoomEntity> filteredRooms = allConsultingRooms.stream()
                .filter(room -> room.getCustomerIds() != null && room.getCustomerIds().contains(customerId))
                .collect(Collectors.toList());


        return filteredRooms.stream()
                        .map(room->{
                            Optional<CategoryEntity> categoryEntity = this.categoryRepository.findById(room.getCategoryId());
                            Optional<CustomerEntity> customerEntity = this.customerRepository.findById(customerId);
                            return ConsultingListDto.builder()
                                    .consultingRoomId(room.getConsultingRoomId())
                                    .createdAt(room.getCreatedAt())
                                    .title(room.getTitle())
                                    .customerName(customerEntity.get().getName())
                                    .categoryName(categoryEntity.get().getCategoryName())
                                    .build();
                        }).collect(Collectors.toList());
    }
}
