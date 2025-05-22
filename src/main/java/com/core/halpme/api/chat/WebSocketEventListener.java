package com.core.halpme.api.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketEventListener {

    private static final Map<String, Set<String>> chatRoomSessions = new ConcurrentHashMap<>();

    public static Set<String> getUsersInRoom(String roomId) {
        return chatRoomSessions.getOrDefault(roomId, new HashSet<>());
    }

    @EventListener
    public void handleSessionConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();


        if (principal == null) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                Object sessionUser = sessionAttributes.get("user");
                if (sessionUser instanceof Principal p) {
                    principal = p;
                }
            }
        }

        if (principal == null) {
            log.warn(">> WebSocket 연결됨. 근데 Principal 못 가져옴. 세션 ID: {}", accessor.getSessionId());
            return;
        }

        String email = principal.getName();
        String roomId = accessor.getFirstNativeHeader("roomId");

        if (roomId != null) {
            WebSocketEventListener.chatRoomSessions
                    .computeIfAbsent(roomId, k -> new HashSet<>())
                    .add(email);
            log.info(">> {} 입장: {}", roomId, email);
        } else {
            log.warn(">> WebSocket 연결 시 roomId 없음. email: {}", email);
        }
    }




    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();
        if (principal == null) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                Object sessionUser = sessionAttributes.get("user");
                if (sessionUser instanceof Principal p) {
                    principal = p;
                }
            }
        }


        if (principal == null) {
            log.warn(">> WebSocket 종료됨. 근데 Principal 못 가져옴. 세션 ID: {}", accessor.getSessionId());
            return;
        }

        String email = principal.getName();
        String roomId = accessor.getFirstNativeHeader("roomId");

        if (roomId != null) {
            Set<String> users = chatRoomSessions.getOrDefault(roomId, new HashSet<>());
            users.remove(email);
            log.info(">> {} 퇴장: {}", roomId, email);
        } else {
            log.warn(">> WebSocket 종료 시 roomId 없음. email: {}", email);
        }
    }

}
