package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private int age;
    private User.Gender gender; // User.Gender 타입
    private String specialNote;
    private User.UserType userType; // User.UserType 타입
    private String addressDetail;
    private double latitute;
    private double longitude;
}