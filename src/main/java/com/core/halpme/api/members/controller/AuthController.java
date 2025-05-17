package com.core.halpme.api.members.controller;

import com.core.halpme.api.members.dto.LoginRequest;
import com.core.halpme.api.members.dto.RegisterRequest;
import com.core.halpme.api.members.service.UserService;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<com.core.halpme.common.response.ApiResponse<Void>> signup(@RequestBody RegisterRequest request) {
        userService.signup(request);
        return com.core.halpme.common.response.ApiResponse.successOnly(SuccessStatus.USER_SIGNUP_SUCCESS);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 성공 시 JWT 토큰을 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 반환"),
            @ApiResponse(responseCode = "401", description = "잘못된 이메일, 패스워드 입력"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @PostMapping("/login")
    public ResponseEntity<com.core.halpme.common.response.ApiResponse<String>> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return com.core.halpme.common.response.ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, token);
    }

    @GetMapping("/test")
    @Operation(summary = "JWT 인증 테스트", description = "토큰 인증이 성공하면 메시지를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<com.core.halpme.common.response.ApiResponse<String>> authTest() {
        return com.core.halpme.common.response.ApiResponse.success(SuccessStatus.AUTH_SUCCESS, "인증에 성공했습니다!");
    }
}