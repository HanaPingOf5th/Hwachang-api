package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.applicationForm.ApplicationForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="ApplicationForm")
public class ApplicationFormEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "application_form_id")
    public UUID applicationFormId;

    @Column(name = "category_id")
    public UUID categoryId;

    @Column(name="title")
    public String title;

    @Column(name="application_form")
    @JdbcTypeCode(SqlTypes.JSON)
    private ApplicationForm applicationForm;
}
