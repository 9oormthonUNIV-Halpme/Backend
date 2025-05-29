package com.core.halpme.api.rank.dto;

import com.core.halpme.api.rank.entity.Rank;
import com.core.halpme.api.rank.entity.RankLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankResponseDto {
    private String nickname;
    private int totalVolunteerHours;
    private RankLevel rankLevel;
    private int myRank;

    public static RankResponseDto fromEntity(Rank rank, int myRank) {
        return RankResponseDto.builder()
                .nickname(rank.getMember().getNickname())
                .totalVolunteerHours(rank.getTotalVolunteerHours())
                .rankLevel(rank.getRankLevel())
                .myRank(myRank)
                .build();
    }
}
