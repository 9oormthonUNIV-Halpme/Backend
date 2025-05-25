package com.core.halpme.api.members.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Address {

    @Column(name = "zip_code")
    private String zipCode;         // 우편번호

    @Column(name = "basic_address")
    private String basicAddress;    // 도로명/지번 주소

    @Column(name = "detail_address")
    private String detailAddress;   // 상세주소

    @Column(name = "direction")
    private String direction;       // 찾아오시는 길
}
