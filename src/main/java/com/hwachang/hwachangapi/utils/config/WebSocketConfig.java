package com.hwachang.hwachangapi.utils.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/consulting-room") // client에서 접속하는 endPoint ex) localhost:8080/consulting-room
                .setAllowedOriginPatterns("*") // cors 에 따른 설정 ( * 는 모두 허용 )
                .withSockJS();

    }

    //메세지 브로커 등록
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // topic으로 시작된 요청을 구독한 사용자에게 메시지를 broadcast
        registry.setApplicationDestinationPrefixes("/app"); // client가 메시지를 요청 보낼 때 prefix

    }
}
