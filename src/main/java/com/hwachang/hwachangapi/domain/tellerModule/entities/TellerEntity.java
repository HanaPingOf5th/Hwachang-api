package com.hwachang.hwachangapi.domain.tellerModule.entities;

import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teller")
public class TellerEntity extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name="teller_id")
    private UUID id;

    @Column(nullable=false, unique=true)
    private String userName;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private AccountRole accountRole; // ToDo: 논의 필요

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Type type;

    @Column
    private String profileImageUrl;

    // UserDetail 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorityCollection = new ArrayList<GrantedAuthority>();
        authorityCollection.add(
                (GrantedAuthority) () -> accountRole.getKey()
        );
        return authorityCollection;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
