package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTotalListResponseDto {

    private Long postId;
    private String title;
    private Address address;
    private LocalDate requestDate;
    private String requestTime;
    private String createdAt;
    private PostStatus status;

    public static PostTotalListResponseDto toDto(Post post) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return PostTotalListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .requestDate(post.getRequestDate())
                .requestTime(post.getRequestTime())
                .createdAt(post.getCreatedAt().format(formatter))
                .status(post.getPostStatus())
                .build();
    }
}
