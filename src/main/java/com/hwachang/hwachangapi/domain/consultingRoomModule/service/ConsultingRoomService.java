package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.clovaModule.utils.FileUtils;
import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ConsultingRoom;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;

import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.JpaConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import com.hwachang.hwachangapi.utils.adapter.LLMServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final LLMServicePort llmServicePort;
    private final JpaConsultingRoomRepository consultingRoomRepository;

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
    public UUID saveConsultingRoomDetails(UUID consultingRoomId, UUID tellerId, UUID categoryId, List<UUID> customerIds, List<String> recordChat, String voiceUrl, String time) {
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

            consultingRoomEntity.updateConsultingRoomByLLM(
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

}
