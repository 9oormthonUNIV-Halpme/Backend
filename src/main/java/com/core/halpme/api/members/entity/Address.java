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
    private double latitute;
    private double longitude;
}
