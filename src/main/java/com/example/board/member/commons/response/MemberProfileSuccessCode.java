package com.example.board.member.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberProfileSuccessCode implements ApiCode {
    PROFILE_CREATED("MEMBER_PROFILE_201_001", "회원 프로필 생성", HttpStatus.CREATED),
    PROFILE_FOUND("MEMBER_PROFILE_200_001", "회원 프로필 조회", HttpStatus.OK),
    PROFILES_FOUND("MEMBER_PROFILE_200_002", "회원 프로필 목록 조회", HttpStatus.OK),
    PROFILE_UPDATED("MEMBER_PROFILE_200_003", "회원 프로필 변경", HttpStatus.NO_CONTENT),
    PROFILE_DELETED("MEMBER_PROFILE_200_004", "회원 프로필 삭제", HttpStatus.OK),
    NICKNAME_AVAILABILITY_CHECKED("MEMBER_PROFILE_200_005", "회원 닉네임 중복 확인", HttpStatus.OK)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberProfileSuccessCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}