package com.hwachang.hwachangapi.domain.consultingRoomModule.entities;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import com.hwachang.hwachangapi.utils.database.AccountRole;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    public UUID categoryId;

    @Column(name = "category_name")
    public String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    public Type categoryType;

}
