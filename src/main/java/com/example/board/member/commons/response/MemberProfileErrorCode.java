package com.example.board.member.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberProfileErrorCode implements ApiCode {
    PROFILE_NOT_FOUND("MEMBER_PROFILE_404_001", "존재하지 않는 프로필입니다.", HttpStatus.NOT_FOUND),
    HANDLE_DUPLICATED("MEMBER_PROFILE_409_001", "이미 사용 중인 핸들입니다.", HttpStatus.CONFLICT),
    NICKNAME_DUPLICATED("MEMBER_PROFILE_409_002", "이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberProfileErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
