package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.Gender;
import com.core.halpme.api.members.entity.MemberType;
import com.core.halpme.api.members.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String phoneNumber;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하이어야 합니다.")
    private int age;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @Size(max = 100, message = "특이사항은 100자 이내여야 합니다.")
    private String specialNote;

    @NotNull(message = "회원 유형은 필수입니다.")
    private MemberType memberType;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String addressDetail;

    @NotBlank(message = "도시는 필수입니다.")
    private String city;

    @NotBlank(message = "구/군은 필수입니다.")
    private String district;

    @NotBlank(message = "동/읍/면은 필수입니다.")
    private String dong;

    @NotNull(message = "권한은 필수입니다.")
    private Role role;
}