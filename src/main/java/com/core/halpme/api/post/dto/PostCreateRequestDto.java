package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.entity.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    @NotNull(message = "주소는 필수 입력 사항입니다.")
    private Address address;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;

    @NotNull
    @Schema(type = "string", example = "14:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;

    @NotNull
    @Schema(type = "string", example = "16:30")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;
}