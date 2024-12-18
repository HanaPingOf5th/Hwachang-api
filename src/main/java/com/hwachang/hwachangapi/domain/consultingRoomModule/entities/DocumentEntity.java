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
@Table(name = "Document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="document_id")
    private UUID documentId;

    @Column(name="category_id")
    private UUID categoryId;

    @Column(name="title")
    private String title;

    @Column(name="path")
    private String path;
}