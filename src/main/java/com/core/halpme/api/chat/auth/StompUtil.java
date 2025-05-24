package com.core.halpme.api.chat.auth;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.Principal;

public class StompUtil {

    public static String getSenderEmail(Message<?> rawMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(rawMessage);
        Principal principal = accessor.getUser();

        if (principal != null) {
            return principal.getName();
        }

        // fallback: 세션에서 사용자 꺼내기
        Object sessionUser = accessor.getSessionAttributes().get("user");
        if (sessionUser instanceof Principal p) {
            return p.getName();
        }

        throw new RuntimeException("메시지 발신자 정보가 없습니다.");
    }
}
