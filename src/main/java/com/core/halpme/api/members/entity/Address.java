package com.core.halpme.api.members.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Address {
    private String city; // 시
    private String district; // 구
    private String dong; // 동
    private String addressDetail; // 상세주소
}
