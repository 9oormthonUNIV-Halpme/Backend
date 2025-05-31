package com.core.halpme.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReadStatusMessage {
    private String readerEmail;
    private List<Long> readMessageIds;
}