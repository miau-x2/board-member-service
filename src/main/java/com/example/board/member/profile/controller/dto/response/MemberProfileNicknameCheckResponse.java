package com.example.board.member.profile.controller.dto.response;

public record MemberProfileNicknameCheckResponse(boolean isAvailable, String message) {
    public static MemberProfileNicknameCheckResponse available(String message) {
        return new MemberProfileNicknameCheckResponse(true, message);
    }
    public static MemberProfileNicknameCheckResponse used(String message) {
        return new MemberProfileNicknameCheckResponse(false, message);
    }
}
