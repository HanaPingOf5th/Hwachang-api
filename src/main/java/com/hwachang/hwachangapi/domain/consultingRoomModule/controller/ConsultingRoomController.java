package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConsultingRoomController {

    //camKey는 공유되어야 함. 나의 camKey를 받아오기 위해 신호를 보냄
    @MessageMapping("/call/key")
    @SendTo("/topic/call/key")
    public String callKey(@Payload String message){
        log.info("[Key] : {}", message);
        return message;
    }

    //신호를 받은 client에서 여기로 key를 보냄 나의 camKey를 연결된 topic에 보냄
    @MessageMapping("/send/key")
    @SendTo("/topic/send/key")
    public String sendKey(@Payload String message){
        return message;
    }


    @MessageMapping("/peer/offer/{camKey}/{roomId}")
    @SendTo("/topic/peer/offer/{camKey}/{roomId}")
    public String PeerHandleOffer(@Payload String offer, @DestinationVariable(value = "roomId") String roomId,
                                  @DestinationVariable(value = "camKey") String camKey) {
        log.info("[OFFER] {} : {}", camKey, offer);
        return offer;
    }

    @MessageMapping("/peer/iceCandidate/{camKey}/{roomId}")
    @SendTo("/topic/peer/iceCandidate/{camKey}/{roomId}")
    public String PeerHandleIceCandidate(@Payload String candidate, @DestinationVariable(value = "roomId") String roomId,
                                         @DestinationVariable(value = "camKey") String camKey) {
        log.info("[ICECANDIDATE] {} : {}", camKey, candidate);
        return candidate;
    }

    @MessageMapping("/peer/answer/{camKey}/{roomId}")
    @SendTo("/topic/peer/answer/{camKey}/{roomId}")
    public String PeerHandleAnswer(@Payload String answer, @DestinationVariable(value = "roomId") String roomId,
                                   @DestinationVariable(value = "camKey") String camKey) {
        log.info("[ANSWER] {} : {}", camKey, answer);
        return answer;
    }

    @MessageMapping("/peer/disconnect/{roomId}")
    @SendTo("/topic/peer/disconnect/{roomId}")
    public String PeerHandleDisconnect(@Payload String message,
                                       @DestinationVariable(value = "roomId") String roomId) {
        log.info("[DISCONNECT] left room {}", roomId);
        return message;
    }

    @MessageMapping("/peer/shareScreen/{roomId}")
    @SendTo("/topic/peer/shareScreen/{roomId}")
    public String PeerHandleShareScreen(@Payload String message,
                                       @DestinationVariable(value = "roomId") String roomId) {
        log.info("[shareScreen] room {} share screen", roomId);
        return message;
    }

    @MessageMapping("/peer/chat/message/{roomId}")
    @SendTo("/topic/peer/chat/message/{roomId}")
    public String message(@Payload String message, @DestinationVariable(value = "roomId") String roomId) {
        log.info("[chat] room {} message {}", roomId, message);
        return message;

    }




}
