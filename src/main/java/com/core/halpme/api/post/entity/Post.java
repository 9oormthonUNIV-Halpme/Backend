package com.core.halpme.api.post.entity;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    // 게시글 제목
    @Column(name = "title")
    private String title;

    // 게시글 내용
    @Column(name = "content")
    private String content;

    // 봉사 요청 날짜
    @Column(name = "request_date")
    private LocalDate requestDate;

    // 봉사 요청 시작 시각
    @Column(name = "request_time")
    private String requestTime;

    // 게시글 봉사 현황 (대기, 완료, 취소)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.WAITING;

    // 주소 (우편번호, 도로명/지번 주소, 상세주소, 찾아오시는 길)
    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Post-Member 양방향 연관관계 값 설정 메서드
    public void setMember(Member member) {
        this.member = member;
        if (!member.getPosts().contains(this)) {
            member.getPosts().add(this);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void updateRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public void updateRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void updateActivityStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }
}
