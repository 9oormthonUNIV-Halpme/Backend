package com.core.halpme.api.participation.entity;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //신청한 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //신청된 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //신청상태
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    public static Participation of(Post post, Member member) {
        return Participation.builder()
                .member(member)
                .post(post)
                .status(ParticipationStatus.PENDING)
                .build();
    }

}
