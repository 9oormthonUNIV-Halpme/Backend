package com.core.halpme.api.rank.entity;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "ranks")
public class Rank extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int totalVolunteerHours;

    @Enumerated(EnumType.STRING)
    private RankLevel rankLevel;

    // 봉사 기록 갱신 - 봉사시간 추가
    public void updateVolunteerRecord(int additionalHours) {
        this.totalVolunteerHours += additionalHours;
        updateRankLevel();  // 추가된 시간 기준으로 랭크 업데이트
    }

    // 누적 봉사시간 기준 레벨 업데이트
    public void updateRankLevel() {
        if (totalVolunteerHours >= 100) {
            this.rankLevel = RankLevel.MASTER;
        } else if (totalVolunteerHours >= 50) {
            this.rankLevel = RankLevel.EXPERT;
        } else if (totalVolunteerHours >= 30) {
            this.rankLevel = RankLevel.SENIOR;
        } else if (totalVolunteerHours >= 10) {
            this.rankLevel = RankLevel.JUNIOR;
        } else {
            this.rankLevel = RankLevel.BEGINNER;
        }
    }

}
