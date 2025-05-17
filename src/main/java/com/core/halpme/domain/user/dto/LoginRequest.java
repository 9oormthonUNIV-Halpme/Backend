package com.core.halpme.domain.user.dto;

import com.core.halpme.domain.Address;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String pw;
}