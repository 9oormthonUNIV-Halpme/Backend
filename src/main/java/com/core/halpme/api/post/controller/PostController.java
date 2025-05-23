package com.core.halpme.api.post.controller;

import com.core.halpme.api.post.dto.MyPostListResponseDto;
import com.core.halpme.api.post.dto.PostCreateRequestDto;
import com.core.halpme.api.post.dto.PostDetailResponseDto;
import com.core.halpme.api.post.dto.PostTotalListResponseDto;
import com.core.halpme.api.post.service.PostService;
import com.core.halpme.common.exception.BadRequestException;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.ErrorStatus;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Post", description = "봉사 신청글 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "봉사 신청글 등록",
            description = "새로운 봉사 신청글을 등록합니다"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "봉사 신청글 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다.")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPost(@Valid @RequestBody PostCreateRequestDto request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        postService.createPost(email, request);

        return ApiResponse.successOnly(SuccessStatus.ARTICLE_CREATE_SUCCESS);
    }


    @Operation(
            summary = "내 봉사 신청글 전체 조회",
            description = "내가 작성한 봉사 신청글을 모두 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "내 봉사 신청글 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다.")
    })
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<MyPostListResponseDto>>> getMyPost() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<MyPostListResponseDto> myPostList = postService.getMyPostList(email);

        return ApiResponse.success(SuccessStatus.MY_POST_LIST_GET_SUCCESS, myPostList);
    }


    @Operation(
            summary = "전체 봉사 신청글 조회",
            description = "현재 전체 봉사 신청글 목록을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 봉사 신청글 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다.")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostTotalListResponseDto>>> getTotalPosts() {

        List<PostTotalListResponseDto> posts = postService.getTotalPostList();

        return ApiResponse.success(SuccessStatus.ARTICLE_GET_SUCCESS, posts);
    }

    @Operation(
            summary = "봉사 신청글 상세 조회",
            description = "특정 봉사 신청의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "봉사 신청글 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 봉사 신청글을 찾을 수 없습니다.")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPostDetail(@PathVariable Long postId) {

        if (postId == null) {
            throw new BadRequestException(ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getMessage());
        }

        PostDetailResponseDto postDetailList = postService.getPostDetail(postId);

        return ApiResponse.success(SuccessStatus.ARTICLE_GET_SUCCESS, postDetailList);
    }

    @Operation(
            summary = "봉사 신청글 수정",
            description = "기존 봉사 신청글을 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "봉사 신청글 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "작성자와 수정 요청자가 다릅니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 봉사 신청글을 찾을 수 없습니다.")
    })
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostCreateRequestDto request) {

        if (postId == null) {
            throw new BadRequestException(ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        postService.updatePost(postId, email, request);

        return ApiResponse.successOnly(SuccessStatus.ARTICLE_UPDATE_SUCCESS);
    }

    @Operation(
            summary = "봉사 신청글 삭제",
            description = "등록한 봉사 신청글을 삭제합니다"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "봉사 신청글 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "작성자와 삭제 요청자가 다릅니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "봉사 신청글을 찾을 수 없습니다."),
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {

        if (postId == null) {
            throw new BadRequestException(ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        postService.deletePost(postId, email);

        return ApiResponse.successOnly(SuccessStatus.ARTICLE_DELETE_SUCCESS);
    }
}