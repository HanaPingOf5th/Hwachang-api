package com.hwachang.hwachangapi.utils.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody RedisTestDto redisTestDto){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(redisTestDto.getKey(), redisTestDto.getValue());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{key}")
    public ResponseEntity<?> getItem(@PathVariable String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        System.out.println(value);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}

@Getter
class RedisTestDto{
    private String key;
    private String value;
}
