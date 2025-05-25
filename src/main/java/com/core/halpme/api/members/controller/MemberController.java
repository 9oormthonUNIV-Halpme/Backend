package com.core.halpme.api.members.controller;

import com.core.halpme.api.members.dto.LoginRequestDto;
import com.core.halpme.api.members.dto.MemberInfoResponseDto;
import com.core.halpme.api.members.dto.SignupRequestDto;
import com.core.halpme.api.members.dto.ResignRequestDto;
import com.core.halpme.api.members.service.MemberService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Member", description = "Member 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 등록합니다."
    )
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto request) {

        memberService.signupMember(request);

        return ApiResponse.successOnly(SuccessStatus.MEMBER_SIGNUP_SUCCESS);
    }


    @Operation(
            summary = "로그인",
            description = "사용자의 정보를 확인 및 성공 시 토큰을 발급합니다."
    )
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequestDto request) {

        Map<String, Object> result = memberService.loginMember(request);

        return ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, result);
    }


    @Operation(
            summary = "회원탈퇴",
            description = "현재 사용자의 정보를 삭제 탈퇴합니다."
    )
    @DeleteMapping("/auth/resign")
    public ResponseEntity<ApiResponse<Void>> resign(@Valid @RequestBody ResignRequestDto resignRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        memberService.resignMember(email, resignRequestDto.getPassword());

        return ApiResponse.successOnly(SuccessStatus.MEMBER_RESIGN_DELETE_SUCCESS);
    }

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인된 사용자의 정보를 조회합니다."
    )
    @GetMapping("/members/my-page")
    public ResponseEntity<ApiResponse<MemberInfoResponseDto>> getMyInfo() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        MemberInfoResponseDto memberInfo = memberService.getMyInfo(email);

        return ApiResponse.success(SuccessStatus.MEMBER_INFO_GET_SUCCESS, memberInfo);
    }


    @Operation(
            summary = "JWT 인증 테스트",
            description = "토큰 인증이 성공하면 메시지를 반환"
    )
    @GetMapping("/auth/test")
    public ResponseEntity<com.core.halpme.common.response.ApiResponse<String>> authTest() {

        return ApiResponse.success(SuccessStatus.AUTH_SUCCESS, "인증에 성공했습니다!");
    }
}