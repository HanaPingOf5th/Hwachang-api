package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.PreChatRequestDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.PreChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/waiting-room")
@RequiredArgsConstructor
public class PreChatController {
    private final PreChatService preChatService;

    @PostMapping("/prechat")
    public void sendPreChat(@RequestBody PreChatRequestDto preChatRequestDto) {
        this.preChatService.sendPreChat(preChatRequestDto);
    }

    @GetMapping("/prechat/{userName}")
    public List<String> getPreChatsByUserName(@PathVariable String userName) {
        List<String> prechats = this.preChatService.getPreChatsByUserName(userName);
        return prechats;
    }
}
