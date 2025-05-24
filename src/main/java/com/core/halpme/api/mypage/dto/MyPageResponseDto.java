package com.core.halpme.api.mypage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
//마이페이지 전체 응답
public class MyPageResponseDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;

    @NotBlank(message = "성별은 필수입니다.")
    private String gender;

    @NotNull(message = "작성한 글 목록은 필수입니다.")
    private List<MyPostDto> posts;

    @NotNull(message = "신청한 봉사 목록은 필수입니다.")
    private List<MyParticipationDto> participations; // 내가 신청한 봉사
}
