package com.hwachang.hwachangapi.domain.customerModule.entities;

import com.hwachang.hwachangapi.utils.database.AccountRole;
import com.hwachang.hwachangapi.utils.database.BaseEntity;
import com.hwachang.hwachangapi.utils.database.BaseMemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "customer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerEntity extends BaseMemberEntity {

    @Column(nullable = false)
    private String phoneNumber;

    // 전체 필드를 초기화하는 생성자
    public CustomerEntity(
            String username,
            String name,
            String password,
            AccountRole accountRole,
            String phoneNumber
    ) {
        super(username, name, password, accountRole);
        this.phoneNumber = phoneNumber;
    }

    public static CustomerEntity create(
            String username,
            String name,
            String password,
            AccountRole accountRole,
            String phoneNumber
    ) {
        return new CustomerEntity(username, name, password, accountRole, phoneNumber);
    }
}
