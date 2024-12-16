package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="banker_id")
    private UUID bankerId;

    @Column(name="category_id")
    private UUID categoryId;

    @Column(name="customer_list")
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> customerIds;

    @Column(name ="original_text")
    private String originalText;

    @Column(name="summary")
    private String summary;

    @Column(name="record_chat")
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> recordChat;

    @Column(name="voice_record")
    private String voiceRecord;

    @Column(name="title")
    private String title;

    @Column(name="time")
    private String time;
}
