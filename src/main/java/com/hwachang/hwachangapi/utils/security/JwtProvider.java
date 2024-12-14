package com.hwachang.hwachangapi.utils.security;

import com.hwachang.hwachangapi.domain.tellerModule.entities.AccountRole;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtProvider {
    // ToDo: secrete key & expire env 파일로 옮기기
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${refresh.secret}")
    private String refreshSecret;

    private final UserDetailsService userDetailsService;

    @Value("3600")
    private long expireTimeAccessToken;

    @Value("36000")
    private long expireTimeRefreshToken;

    public String createAccessToken(String userPk, String tellerName){
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        claims.put("roles", tellerName);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plus(expireTimeAccessToken, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String createRefreshToken(String userPk, String tellerName){
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        claims.put("validationToken", tellerName);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plus(expireTimeRefreshToken, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, refreshSecret)
                .compact();
    }

    public Authentication authenticate(String token) throws Exception{
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
        if(claims.getExpiration().before(new Date()) || claims.getIssuedAt().after(new Date())){
            throw new Exception("Access Token Expired");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        if(!validateUserDetails(userDetails)){
            throw new Exception("User not valid");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean validateUserDetails(UserDetails userDetails){
        if(userDetails == null ||!userDetails.isEnabled() || !userDetails.isAccountNonExpired()
                || !userDetails.isAccountNonLocked() || !userDetails.isCredentialsNonExpired()){
            return false;
        } else{
            return true;
        }
    }
}
