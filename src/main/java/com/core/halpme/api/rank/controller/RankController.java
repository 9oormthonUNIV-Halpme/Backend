package com.core.halpme.api.rank.controller;

import com.core.halpme.api.rank.dto.RankResponseDto;
import com.core.halpme.api.rank.service.RankService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Top 10 랭킹 조회",
            description = "누적 봉사시간 기준 상위 10명의 랭킹 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top 10 랭킹 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "랭킹 데이터가 존재하지 않습니다.")
    })
    @GetMapping("/top")
    public List<RankResponseDto> getTopRanks() {
        return rankService.getToRanks()
                .stream()
                .map(RankResponseDto::fromEntity)
                .toList();
    }

    // 점수 조회
    @Operation(
            summary = "내 봉사시간(점수) 조회",
            description = "로그인한 사용자의 누적 봉사시간(점수)를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내 봉사시간 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 정보가 존재하지 않습니다.")
    })
    @GetMapping("/my-points")
    public ResponseEntity<ApiResponse<Integer>> getMyPoints() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int hours = rankService.getMyTotalVolunteerHours(email);
        return ApiResponse.success(SuccessStatus.MEMBER_INFO_GET_SUCCESS, hours);
    }
}
