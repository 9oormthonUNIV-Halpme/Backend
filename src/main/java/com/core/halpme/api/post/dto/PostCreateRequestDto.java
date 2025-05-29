package com.core.halpme.api.post.dto;

import com.core.halpme.api.members.entity.Address;
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

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private Address address;

    @NotNull
    private LocalDate requestDate;

    @NotNull
    private LocalTime startHour;

    @NotNull
    private LocalTime endHour;
}
