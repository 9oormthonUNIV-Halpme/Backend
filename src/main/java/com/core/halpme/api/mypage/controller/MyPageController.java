package com.core.halpme.api.mypage.controller;

import com.core.halpme.api.mypage.dto.MyPageResponseDto;
import com.core.halpme.api.mypage.service.MyPageService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageResponseDto>> getPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MyPageResponseDto responseDto = myPageService.getMyPage(email);
        return ApiResponse.success(SuccessStatus.MY_PAGE_GET_SUCCESS, responseDto);
    }

}
