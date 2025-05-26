package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.Address;
import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {

    private String nickname;
    private String phoneNumber;
    private String zipCode;
    private String basicAddress;
    private String detailAddress;
    private String direction;
    private int age;

    public Address toAddress() {
        return Address.builder()
                .zipCode(zipCode)
                .basicAddress(basicAddress)
                .detailAddress(detailAddress)
                .direction(direction)
                .build();
    }
}