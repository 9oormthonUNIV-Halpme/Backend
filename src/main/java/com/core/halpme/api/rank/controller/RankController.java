package com.core.halpme.api.rank.controller;

import com.core.halpme.api.rank.dto.RankResponseDto;
import com.core.halpme.api.rank.service.RankService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vi/rank")
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    // top 10만 뽑는 기능
    @GetMapping("/top")
    public List<RankResponseDto> getTopRanks() {
        return rankService.getToRanks()
                .stream()
                .map(RankResponseDto::fromEntity)
                .toList();
    }

    // 점수 조회
    @GetMapping("/my-points")
    public ResponseEntity<ApiResponse<Integer>> getMyPoints() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int hours = rankService.getMyTotalVolunteerHours(email);
        return ApiResponse.success(SuccessStatus.MEMBER_INFO_GET_SUCCESS, hours);
    }
}
