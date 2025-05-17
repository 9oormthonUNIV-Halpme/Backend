package com.core.halpme.api.post.entity;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;
    private String imageUrl;
    private String userName;
    private String userPhoneNumber;
    private String userSpecialNote;
    private enum PostUserType{User, Requester};

    //activity에 있었던거
    private String activityProof; // 인증사진
    private enum ActivityStatus{Completed, Waiting, Cancelled};


    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

}