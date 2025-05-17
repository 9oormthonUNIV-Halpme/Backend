package com.core.halpme.domain;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Requester {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private int age;
    private enum gender{MALE,FEMALE};
    private String specialNote;
    private enum UserType{Elderly, Guardian}
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "requester")
    private List<Post> posts;

}
