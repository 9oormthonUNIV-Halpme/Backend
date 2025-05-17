package com.core.halpme.domain.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String addressDetail;
    private double latitute;
    private double longitude;
}
