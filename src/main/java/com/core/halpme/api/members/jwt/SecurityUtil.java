package com.core.halpme.api.members.jwt;

import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.response.ErrorStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class SecurityUtil {
    public String getCurrentMemberUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BaseException(
                    ErrorStatus.UNAUTHORIZED_TOKEN_EXPIRED.getHttpStatus(),
                    ErrorStatus.UNAUTHORIZED_TOKEN_EXPIRED.getMessage()
            );
        }

        return authentication.getName();
    }
}