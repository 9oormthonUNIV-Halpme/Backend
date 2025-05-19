package com.core.halpme.api.post.controller;

import com.core.halpme.api.post.dto.PostCreateRequest;
import com.core.halpme.api.post.dto.PostResponse;
import com.core.halpme.api.post.repository.PostRepository;
import com.core.halpme.api.post.service.PostService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시물 생성
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();


        PostResponse response = postService.createPost(email, request);

        // 201 Created 상태 코드와 함께 응답 반환
        return ApiResponse.success(SuccessStatus.ARTICLE_CREATE_SUCCESS, response);  // PostResponse 반환
    }

    //시, 구, 동 기준으로 게시물 조회
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostByAddress(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String dong) {

        //이건 인증이 필요하면 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<PostResponse> posts = postService.findPostByAddress(city, district, dong, email);
        return ApiResponse.success(SuccessStatus.ARTICLE_GET_SUCCESS, posts);
    }

}
