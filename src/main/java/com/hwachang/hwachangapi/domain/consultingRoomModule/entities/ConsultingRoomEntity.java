package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;
import com.hwachang.hwachangapi.utils.annotations.StringListConverter;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ConsultingRoom")
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
    @Convert(converter = StringListConverter.class)
    private List<UUID> customerIds;

    @Column(name ="original_text")
    // @JdbcTypeCode(SqlTypes.JSON) // JSON 타입 저장
    private String originalText;

    @Column(name="summary")
    private String summary; // 요약을 String 형태로 저장

    @Column(name="record_chat")
    @Convert(converter = StringListConverter.class)
    private List<String> recordChat;

    @Column(name="voice_record")
    private String voiceRecord;

    @Column(name="title")
    private String title;

    @Column(name="time")
    private String time;
}
