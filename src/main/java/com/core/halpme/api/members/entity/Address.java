package com.core.halpme.api.members.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Address {
    private String city; // 시
    private String district; // 구
    private String dong; // 동
}
