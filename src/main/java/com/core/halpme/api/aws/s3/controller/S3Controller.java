package com.core.halpme.api.aws.s3.controller;

import com.core.halpme.api.aws.s3.service.S3Service;
import com.core.halpme.common.exception.BadRequestException;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.ErrorStatus;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "S3", description = "S3 이미지 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(
            summary = "이미지 업로드",
            description = "여러 장의 이미지를 S3에 업로드 후 업로드된 이미지의 URL 리스트를 반환"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "업로드 성공, 이미지 URL 리스트 반환"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다.")
    })
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!isImageFile(image)) {
                    throw new BadRequestException(ErrorStatus.BAD_REQUEST_NOT_ALLOW_IMG_MIME.getMessage());
                }
            }
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String emailPrefix = getEmailPrefix(email);

        List<String> imageUrls = s3Service.uploadChatMessageImages(emailPrefix, images);

        return ApiResponse.success(SuccessStatus.IMAGE_UPLOAD_POST_SUCCESS, imageUrls);
    }

    @Operation(
            summary = "이미지 삭제",
            description = "S3에 저장된 이미지를 URL로 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이미지 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "필수 정보가 입력되지 않았습니다.")
    })
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @RequestBody String imageUrl
    ) {

        s3Service.deleteFile(imageUrl);

        return ApiResponse.successOnly(SuccessStatus.IMAGE_DELETE_SUCCESS);
    }
    
    // 이미지 타입(MIME) 검증
    private boolean isImageFile(MultipartFile file) {

        String contentType = file.getContentType();

        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/bmp") ||
                        contentType.equals("image/webp")
        );
    }

    // 이메일 prefix 추출
    private String getEmailPrefix(String email) {
        if (email == null) return "anonymous";
        int atIndex = email.indexOf("@");
        if (atIndex > 0) {
            return email.substring(0, atIndex);
        }
        return email;
    }
}
