package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.dto.AddressDto;
import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTotalListResponseDto {

    private Long postId;
    private String title;
    private String nickname;
    private AddressDto address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;
    private String createdAt;
    private PostStatus status;

    public static PostTotalListResponseDto toDto(Post post) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return PostTotalListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .nickname(post.getMember().getNickname())
                .address(AddressDto.toDto(post.getAddress()))
                .requestDate(post.getRequestDate())
                .startHour(post.getStartHour())
                .endHour(post.getEndHour())
                .createdAt(post.getCreatedAt().format(formatter))
                .status(post.getPostStatus())
                .build();
    }
}
