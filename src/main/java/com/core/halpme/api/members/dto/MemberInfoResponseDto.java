package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfoResponseDto {

    private final String nickname;
    private final String phoneNumber;
    private final int age;
    private final Address address;

    public static MemberInfoResponseDto toDto(Member member) {
        return MemberInfoResponseDto.builder()
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .age(member.getAge())
                .address(member.getAddress())
                .build();
    }
}
