package com.core.halpme.api.members.entity;

import com.core.halpme.api.post.entity.Post;
import com.core.halpme.common.entity.BaseTimeEntity;
import com.core.halpme.domain.Activity;
import com.core.halpme.domain.VolunteerCertification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") //h2 데이터베이스 예약어 문제로 수정
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private int age;
    private String specialNote;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Embedded
    private Address address;

    @OneToMany
    private List<Post> posts = new ArrayList<>();

    public enum Gender {
        MALE, FEMALE
    }

    public enum UserType {
        Elderly, Guardian
    }
}
