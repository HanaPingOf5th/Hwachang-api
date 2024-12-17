package com.hwachang.hwachangapi.utils.config;

import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.database.BaseMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService(BaseMemberRepository memberRepository){
        return username -> memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}