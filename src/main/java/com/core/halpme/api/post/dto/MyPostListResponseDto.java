package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostListResponseDto {

    private Long postId;
    private String title;
    private Address address;
    private LocalDate requestDate;
    private String requestTime;
    private PostStatus postStatus;

    public static MyPostListResponseDto toDto(Post post) {

        return MyPostListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .requestDate(post.getRequestDate())
                .requestTime(post.getRequestTime())
                .postStatus(post.getPostStatus())
                .build();
    }
}
