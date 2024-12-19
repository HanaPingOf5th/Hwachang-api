package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.SubjectedFormData;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.InsertFormDto;
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
@Table(name = "Saved_form")
public class SavedFormEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="Saved_form_id")
    private UUID savedFormId;

    @Column(name = "customer_id")
    public UUID customerId;

    @Column(name = "username")
    public String username;

    @Column(name="subjected_form_data")
    @JdbcTypeCode(SqlTypes.JSON)
    private InsertFormDto insertFormDto;
}
