package com.core.halpme.api.members.entity;

import com.core.halpme.api.post.entity.Post;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

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
