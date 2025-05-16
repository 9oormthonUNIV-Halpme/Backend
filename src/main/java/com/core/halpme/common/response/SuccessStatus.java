package com.core.halpme.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * 200 OK
     */
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    MEMBER_GET_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
    MEMBER_WITHDRAW_SUCCESS(HttpStatus.OK, "회원탈퇴 성공"),
    CURRENT_MEMBER_GET_SUCCESS(HttpStatus.OK, "현재 사용자 정보 조회 성공"),
    ARTICLE_GET_SUCCESS(HttpStatus.OK,"게시글 조회 성공"),
    ARTICLE_UPDATE_SUCCESS(HttpStatus.OK,"게시글 수정 성공"),

    /**
     * 201 CREATED
     */
    ARTICLE_CREATE_SUCCESS(HttpStatus.CREATED, "게시판 등록 성공"),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),

    /**
     * 204 NO CONTENT
     */
    ARTICLE_DELETE_SUCCESS(HttpStatus.NO_CONTENT,"게시글 삭제 성공"),


    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
