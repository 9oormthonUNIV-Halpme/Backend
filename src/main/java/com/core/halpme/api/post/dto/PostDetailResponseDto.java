package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.dto.AddressDto;
import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;

    @Schema(type = "string", example = "14:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;

    @Schema(type = "string", example = "16:30")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;

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
                .startHour(post.getStartHour())
                .endHour(post.getEndHour())
                .date(post.getUpdatedAt().format(formatter))
                .build();
    }
}
