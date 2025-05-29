package com.core.halpme.api.rank.entity;

import lombok.Getter;

@Getter
public enum RankLevel {
    SEED_HELPER("새싹도우미"),
    ACTIVIST("활동가"),
    GUARDIAN("마을지킴이"),
    LOCAL_LEADER("지역리더"),
    HERO("영웅");

    private final String description;

    RankLevel(String description) {
        this.description = description;
    }
}
