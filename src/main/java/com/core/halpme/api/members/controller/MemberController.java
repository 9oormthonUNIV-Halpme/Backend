package com.core.halpme.api.members.controller;

import com.core.halpme.api.members.dto.LoginRequest;
import com.core.halpme.api.members.dto.RegisterRequest;
import com.core.halpme.api.members.service.MemberService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody RegisterRequest request) {
        memberService.signup(request);
        return ApiResponse.successOnly(SuccessStatus.USER_SIGNUP_SUCCESS);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 성공 시 JWT 토큰을 반환")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> result = memberService.login(request);
        return ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, result);
    }

    @GetMapping("/test")
    @Operation(summary = "JWT 인증 테스트", description = "토큰 인증이 성공하면 메시지를 반환")
    public ResponseEntity<com.core.halpme.common.response.ApiResponse<String>> authTest() {
        return ApiResponse.success(SuccessStatus.AUTH_SUCCESS, "인증에 성공했습니다!");
    }
}