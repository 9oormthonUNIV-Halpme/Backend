package com.core.halpme.domain;

import com.core.halpme.domain.Activity;
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
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<Activity> activity;

    @OneToMany(mappedBy = "user")
    private List<VolunteerCertification>  volunteerCertification;




}
