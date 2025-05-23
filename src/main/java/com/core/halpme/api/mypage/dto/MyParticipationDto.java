package com.core.halpme.api.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyParticipationDto {
    @NotBlank(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "도시는 필수입니다.")
    private String city;

    @NotBlank(message = "동은 필수입니다.")
    private String dong;

    @NotBlank(message = "상태는 필수입니다.")
    private String status;
}

