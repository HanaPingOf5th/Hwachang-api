package com.hwachang.hwachangapi.domain.consultingRoom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ConsultingRoom {
    public ConsultingRoom(String roomId){
        this.roomId = roomId;
    }

    @Id
    private String roomId;

    @JdbcTypeCode(SqlTypes.JSON)
    private ChatRecord chatRecord;
}
