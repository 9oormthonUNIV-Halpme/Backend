package com.core.halpme.api.post.entity;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.common.entity.BaseTimeEntity;
import com.core.halpme.common.exception.ConflictException;
import com.core.halpme.common.response.ErrorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @Column(name = "start_hour")
    private LocalTime startHour;

    // 봉사 요청 끝 시각
    @Column(name = "end_hour")
    private LocalTime endHour;

    // 게시글 봉사 현황 (대기, 완료, 취소)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.WAITING;

    // 주소 (우편번호, 도로명/지번 주소, 상세주소, 찾아오시는 길)
    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 봉사 신청자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Member volunteer;       // 봉사 참여자

    // Post-Member 양방향 연관관계 값 설정 메서드
    public void setMember(Member member) {
        this.member = member;
        if (!member.getPosts().contains(this)) {
            member.getPosts().add(this);
        }
    }
    
    // 봉사자를 봉사 참여글과 매핑
    // 이미 배정되었다면 예외 발생, 그렇지 않다면 봉사자를 할당하며 상태를 완료로 변경
    public void assignVolunteer(Member volunteer) {

        if (this.volunteer != null) {
            throw new ConflictException(ErrorStatus.BAD_REQUEST_ALREADY_ASSIGNED_VOLUNTEER.getMessage());
        }

        this.volunteer = volunteer;
        this.postStatus = PostStatus.COMPLETED;
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

    public void updateStartTime(LocalTime startHour) {this.startHour = startHour; }

    public void updateEndTime(LocalTime endHour) {this.endHour = endHour; }

    public void updateActivityStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    //봉사시간 계산 로직 endTime - startTime
    public int calculateVolunteerHours() {
        if(this.startHour == null || this.endHour == null) {
            throw new IllegalArgumentException("시작/종료 시간이 없습니다.");
        }
        if(!this.endHour.isAfter(this.startHour)) {
            throw new IllegalArgumentException("종료 시간은 시작시간보다 늦어야합니다.");
        }
        //봉사시간 int 형태로 바꾸고 반환
        return (int) Duration.between(this.startHour, this.endHour).toHours();
    }
}
