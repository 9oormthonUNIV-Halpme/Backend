package com.core.halpme.api.members.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Address {
    private String addressDetail;
    private String city; // 시
    private String district; // 구
    private String dong; // 동
}
