package com.core.halpme.domain.user.entity;

import com.core.halpme.domain.Activity;
import com.core.halpme.domain.Address;
import com.core.halpme.domain.VolunteerCertification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
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
