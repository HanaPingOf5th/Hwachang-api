package com.hwachang.hwachangapi.utils.database;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public abstract class BaseMemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    protected UUID id;

    @Column(nullable=false, unique=true)
    protected String username;

    @Column(nullable=false)
    protected String name;

    @Column(nullable=false)
    protected String password;

    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    protected AccountRole accountRole;

    protected BaseMemberEntity(String username, String name, String password, AccountRole accountRole) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.accountRole = accountRole;
    }

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
