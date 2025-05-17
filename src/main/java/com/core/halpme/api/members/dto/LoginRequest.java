package com.core.halpme.api.members.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String pw;
}