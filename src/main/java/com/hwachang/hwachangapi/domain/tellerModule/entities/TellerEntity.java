package com.hwachang.hwachangapi.domain.tellerModule.entities;

import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.StatusHandler;
import com.hwachang.hwachangapi.utils.database.AccountRole;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import com.hwachang.hwachangapi.utils.database.BaseMemberEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Table(name = "teller")
@NoArgsConstructor
public class TellerEntity extends BaseMemberEntity {
    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Type type;

    @Column
    private String profileImageUrl;

    public TellerEntity(
            String username,
            String name,
            String password,
            AccountRole accountRole,
            String position,
            Status status,
            Type type,
            String profileImageUrl
    ) {
        super(username, name, password, accountRole); // 상위 클래스의 생성자 호출
        this.position = position;
        this.status = status;
        this.type = type;
        this.profileImageUrl = profileImageUrl;
    }

    public static TellerEntity create(
            String username,
            String name,
            String password,
            AccountRole accountRole,
            String position,
            Status status,
            Type type,
            String profileImageUrl
    ) {

        return new TellerEntity(username, name, password, accountRole, position, status, type, profileImageUrl);
    }

    public void changeStatus(Status newStatus) {
        if (newStatus == null) {
            throw new StatusHandler(ErrorStatus.STATUS_NOT_FOUND);
        }
        this.status = newStatus;
    }
}
