package com.core.halpme.api.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
//MyPageResponseDto안에 포함되는
public class MyPostDto {
    @NotBlank(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
