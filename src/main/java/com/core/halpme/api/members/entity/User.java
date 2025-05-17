package com.core.halpme.api.members.entity;

import com.core.halpme.api.post.entity.Post;
import com.core.halpme.common.entity.BaseTimeEntity;
import com.core.halpme.domain.Activity;
import com.core.halpme.domain.VolunteerCertification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private int age;
    private enum gender{MALE,FEMALE};
    private String specialNote;
    private enum UserType{Elderly, Guardian}

    @Embedded
    private Address address;

    @OneToMany
    private List<Post> posts = new ArrayList<>();
}
