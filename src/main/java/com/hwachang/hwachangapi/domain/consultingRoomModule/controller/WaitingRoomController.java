package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.PreChatRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/waiting-room")
@RequiredArgsConstructor
public class WaitingRoomController {
    private final RedisTemplate<String, List<String>> redisListTemplate;
    @PostMapping("/prechat")
    public void sendPreChat (@RequestBody PreChatRequestDto preChatRequestDto){
        ValueOperations<String, List<String>> valueOperations = redisListTemplate.opsForValue();
        String userName = preChatRequestDto.getUserName();
        String content = preChatRequestDto.getContent();

        List<String> prechats = valueOperations.get(userName);
        if (prechats == null) {
            prechats = new ArrayList<>();
        }
        prechats.add(content);
        Long expiredTime = 600L;
        valueOperations.set(userName, prechats, expiredTime, TimeUnit.SECONDS);
    }

    @GetMapping("/prechat/{userName}")
    public List<String> getPreChatsByUserName(@PathVariable String userName){
        ValueOperations<String, List<String>> valueOperations = redisListTemplate.opsForValue();
        List<String> prechats = valueOperations.get(userName);
        return prechats;
    }
}
