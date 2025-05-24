package com.core.halpme.api.members.dto;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Gender;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하이어야 합니다.")
    private int age;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    private Address address;

    @NotNull(message = "권한은 필수입니다.")
    private Role role;

    // DTO -> Member 엔티티
    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .nickname(nickname)
                .password(encodedPassword)
                .email(email)
                .phoneNumber(phoneNumber)
                .age(age)
                .gender(gender)
                .role(role)
                .address(address)
                .build();
    }
}
