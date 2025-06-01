package com.core.halpme.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpponentInfoDto {
    private String opponentNickname;
    private String identity;
    private String opponentEmail;
}