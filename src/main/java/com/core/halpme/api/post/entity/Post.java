package com.core.halpme.api.post.entity;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.domain.Requester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
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

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Requester requester;

}
