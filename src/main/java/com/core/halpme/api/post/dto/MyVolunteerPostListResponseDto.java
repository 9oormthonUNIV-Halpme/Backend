package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.dto.AddressDto;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyVolunteerPostListResponseDto {

    private Long postId;
    private String title;
    private String nickname;
    private AddressDto address;
    private LocalDate requestDate;
    private LocalTime startHour;
    private LocalTime endHour;
    private PostStatus postStatus;

    public static MyVolunteerPostListResponseDto toDto(Post post) {

        return MyVolunteerPostListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .nickname(post.getMember().getNickname())
                .address(AddressDto.toDto(post.getAddress()))
                .requestDate(post.getRequestDate())
                .startHour(post.getStartHour())
                .endHour(post.getEndHour())
                .postStatus(post.getPostStatus())
                .build();
    }
}

