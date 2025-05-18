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
        this.city = post.getCity();
        this.district = post.getDistrict();
        this.dong = post.getDong();
        this.username = post.getMember().getUsername();
    }
}
