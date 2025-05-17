package com.core.halpme.domain;

import com.core.halpme.api.members.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;
    private String activityProof; // 인증사진
    private LocalDateTime verifiedAt;
    private enum ActivityStatus{Completed, Waiting, Cancelled};

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private VolunteerCertification volunteerCertification;
}
