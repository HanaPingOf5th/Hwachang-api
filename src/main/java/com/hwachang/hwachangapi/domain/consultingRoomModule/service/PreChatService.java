package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.PreChatRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PreChatService {
    private final RedisTemplate<String, List<String>> redisListTemplate;

    public void sendPreChat(PreChatRequestDto preChatRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        String content = preChatRequestDto.getContent();

        ValueOperations<String, List<String>> valueOperations = redisListTemplate.opsForValue();
        List<String> prechats = valueOperations.get(userName);
        if (prechats == null) {
            prechats = new ArrayList<>();
        }
        prechats.add(content);
        Long expiredTime = 600L;
        valueOperations.set(userName, prechats, expiredTime, TimeUnit.SECONDS);
    }

    public List<String> getPreChatsByUserName(String userName) {
        ValueOperations<String, List<String>> valueOperations = redisListTemplate.opsForValue();
        List<String> prechats = valueOperations.get(userName);
        if (prechats == null) {
            prechats = new ArrayList<>();
        }
        return prechats;
    }
}
