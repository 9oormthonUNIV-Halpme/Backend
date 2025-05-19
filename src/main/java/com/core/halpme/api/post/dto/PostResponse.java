package com.core.halpme.api.post.dto;

import com.core.halpme.api.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String city;
    private String district;
    private String dong;
    private String username;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.city = post.getAddress().getCity();
        this.district = post.getAddress().getDistrict();
        this.dong = post.getAddress().getDong();
        this.username = post.getMember().getNickname();
    }
}
