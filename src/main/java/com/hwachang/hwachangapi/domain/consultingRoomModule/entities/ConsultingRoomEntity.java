package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;
import com.hwachang.hwachangapi.utils.annotations.JsonListConverter;
import com.hwachang.hwachangapi.utils.annotations.StringListConverter;
import com.hwachang.hwachangapi.utils.annotations.UniversalListConverter;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Consulting_room")
public class ConsultingRoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="consulting_room_id")
    private UUID consultingRoomId;

    @Column(name="teller_id")
    private UUID tellerId;

    @Column(name="category_id")
    private UUID categoryId;

    @Column(name="customer_list")
    @Convert(converter = UniversalListConverter.class)
    private List<UUID> customerIds;

    @Column(name = "original_text")
    @Convert(converter = JsonListConverter.class) // JSON 변환 컨버터 적용
    private List<Map<String, Object>> originalText;

    @Column(name="summary")
    private String summary; // 요약을 String 형태로 저장

    @Column(name="record_chat")
    @Convert(converter = UniversalListConverter.class)
    private List<String> recordChat;

    @Column(name="voice_record_url")
    private String voiceRecordUrl;

    @Column(name="title")
    private String title;

    @Column(name="time")
    private String time;

    public void updateConsultingRoomDetails(ConsultingRoomEntity consultingRoomEntity) {
        this.consultingRoomId = consultingRoomEntity.getConsultingRoomId();
        this.tellerId = consultingRoomEntity.getTellerId();
        this.categoryId = consultingRoomEntity.getCategoryId();
        this.customerIds = consultingRoomEntity.getCustomerIds();
        this.originalText = consultingRoomEntity.getOriginalText();
        this.summary = consultingRoomEntity.getSummary();
        this.recordChat = consultingRoomEntity.getRecordChat();
        this.voiceRecordUrl = consultingRoomEntity.getVoiceRecordUrl();
        this.title = consultingRoomEntity.getTitle();
        this.time = consultingRoomEntity.getTime();
    }
}
