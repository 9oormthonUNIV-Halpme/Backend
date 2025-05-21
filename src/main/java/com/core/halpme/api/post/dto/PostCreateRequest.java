package com.core.halpme.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

//게시물 생성 요청
@Getter
public class PostCreateRequest {
    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    @NotBlank(message = "시 정보는 필수 입력 사항입니다.")
    private String city;

    @NotBlank(message = "구 정보는 필수 입력 사항입니다.")
    private String district;

    @NotBlank(message = "동 정보는 필수 입력 사항입니다.")
    private String dong;

    @NotBlank(message = "상세 정보는 필수 입력 사항입니다.")
    private String AddressDetail;

}
