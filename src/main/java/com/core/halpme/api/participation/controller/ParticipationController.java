package com.core.halpme.api.participation.controller;

import com.core.halpme.api.participation.dto.ParticipationResponseDto;
import com.core.halpme.api.participation.service.ParticipationService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    // 봉사 신청
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> applyToPost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        participationService.applyToPost(email, postId);
        return ApiResponse.successOnly(SuccessStatus.PARTICIPATION_APPLY_SUCCESS);
    }

    // 내가 신청한 봉사 보기
    @GetMapping("/myapplication")
    public ResponseEntity<ApiResponse<List<ParticipationResponseDto>>> getMyApplications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<ParticipationResponseDto> participations = participationService.getMyApplications(email);
        return ApiResponse.success(SuccessStatus.PARTICIPATION_GET_SUCCESS, participations);
    }
}