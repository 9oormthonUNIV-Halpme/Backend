package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.dto.AddressDto;
import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponseDto {

    private Long postId;
    private String title;
    private String nickname;
    private String content;
    private AddressDto address;
    private LocalDate requestDate;
    private String requestTime;
    private String date;

    public static PostDetailResponseDto toDto(Post post) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .nickname(post.getMember().getNickname())
                .content(post.getContent())
                .address(AddressDto.toDto(post.getAddress()))
                .requestDate(post.getRequestDate())
                .requestTime(post.getRequestTime())
                .date(post.getUpdatedAt().format(formatter))
                .build();
    }
}
