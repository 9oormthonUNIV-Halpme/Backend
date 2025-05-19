package com.core.halpme.api.chat.auth;

import com.core.halpme.api.members.jwt.JwtTokenProvider;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("WebSocket connect Authorization header: {}", authHeader);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new BaseException(ErrorStatus.UNAUTHORIZED_EMPTY_TOKEN.getHttpStatus(), "헤더 없음");
            }

            String token = authHeader.substring(7); // "Bearer " 제거

            if (!jwtTokenProvider.validateToken(token)) {
                throw new BaseException(ErrorStatus.UNAUTHORIZED_INVALID_TOKEN.getHttpStatus(), "토큰 무효");
            }

            String email = jwtTokenProvider.getEmail(token);
            accessor.setUser(new StompPrincipal(email)); // 나중에 accessor.getUser()로 꺼내쓸 수 있음
        }

        return message;
    }
}