package com.core.halpme.api.participation.dto;

import com.core.halpme.api.participation.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationResponseDto {
    private Long postId;
    private String title;
    private String city;
    private String dong;
    private String status;

    public ParticipationResponseDto(Participation participation) {
        this.postId = participation.getPost().getId();
        this.title = participation.getPost().getTitle();
        this.city = participation.getPost().getAddress().getCity();
        this.city = participation.getPost().getAddress().getDong();
        this.status = participation.getStatus().name();
    }
}
