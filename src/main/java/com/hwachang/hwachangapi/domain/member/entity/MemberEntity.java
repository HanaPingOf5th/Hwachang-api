package com.hwachang.hwachangapi.domain.member.entity;

import com.hwachang.hwachangapi.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="member_type")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class MemberEntity extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String validationToken;

    @Column(nullable=false)
    private Boolean isAccountNonExpired;

    @Column(nullable=false)
    private Boolean isAccountNonLocked;

    @Column(nullable=false)
    private Boolean isCredentialsNonExpired;

    @Column(nullable=false)
    private Boolean isEnabled;

    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private AccountRole accountRole;

    // UserDetail 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorityCollection = new ArrayList<GrantedAuthority>();
        authorityCollection.add(
                (GrantedAuthority) () -> accountRole.getKey()
        );
        return authorityCollection;
    }

    public void setValidationToken(String validationToken) {
        this.validationToken = validationToken;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
