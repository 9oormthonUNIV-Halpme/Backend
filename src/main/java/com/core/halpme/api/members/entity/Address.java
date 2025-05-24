package com.core.halpme.api.members.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Address {
    private String city; // 시
    private String district; // 구
    private String dong; // 동

    // 상세주소 (아파트 동, 호수, 번지)인데 여기서는 필요없음 -> 추가되면 생각
    private String addressDetail;
}
