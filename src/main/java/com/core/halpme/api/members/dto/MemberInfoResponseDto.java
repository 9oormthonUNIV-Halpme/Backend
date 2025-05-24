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

    private final Long id;
    private final String email;
    private final String nickname;
    private final String phoneNumber;
    private final String gender;
    private final int age;
    private final Address address;
    private final String note;

    public static MemberInfoResponseDto toDto(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .gender(member.getGender().name())
                .age(member.getAge())
                .address(member.getAddress())
                .note(member.getNote())
                .build();
    }

}
