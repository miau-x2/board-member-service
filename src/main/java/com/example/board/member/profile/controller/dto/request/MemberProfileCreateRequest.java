package com.example.board.member.profile.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberProfileCreateRequest(
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 5, max = 20, message = "아이디는 5~20자입니다.")
        String username,
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 20, message = "닉네임은 2~20자입니다.")
        @Pattern(
                regexp = "^[a-z0-9가-힣]+$",
                message = "닉네임은 한글, 영문 소문자, 숫자만 사용할 수 있습니다."
        )
        String nickname
) {
}