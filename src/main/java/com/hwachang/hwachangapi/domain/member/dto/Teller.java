package com.hwachang.hwachangapi.domain.member.dto;

import com.hwachang.hwachangapi.domain.member.entity.AccountRole;
import lombok.*;

@Builder
@Getter
public class Teller {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String salt;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;
    private AccountRole accountRole;
}
