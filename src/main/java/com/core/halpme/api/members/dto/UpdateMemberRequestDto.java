package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.Address;
import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {

    private String nickname;
    private String phoneNumber;
    private Address address;
    private int age;

}