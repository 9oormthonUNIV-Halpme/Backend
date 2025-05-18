package com.core.halpme.api.post.dto;

import lombok.Getter;

//게시물 생성 요청
@Getter
public class PostCreateRequest {
    private String title;
    private String content;
    private String city;
    private String district;
    private String dong;

}
