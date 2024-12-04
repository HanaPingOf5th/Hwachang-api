package com.hwachang.hwachangapi.domain.member.entity;

import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@DiscriminatorValue("teller")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class TellerEntity extends MemberEntity{

    public static TellerEntity createTellerEntity(TellerRegisterRequestDto teller){
        TellerEntity tellerEntity = new TellerEntity();
        tellerEntity.setUsername(teller.getUserName());
        tellerEntity.setName(teller.getName());
        tellerEntity.setAccountRole(AccountRole.Teller);
        tellerEntity.setPassword(teller.getPassword());
        tellerEntity.setValidationToken("-1");
        tellerEntity.setIsAccountNonExpired(true);
        tellerEntity.setIsAccountNonLocked(true);
        tellerEntity.setIsCredentialsNonExpired(true);
        tellerEntity.setIsEnabled(true);

        return tellerEntity;
    }
}