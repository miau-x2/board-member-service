package com.example.board.member.profile.service.result;

public sealed interface MemberProfileResult {
    sealed interface Create extends MemberProfileResult {
        record Success() implements Create {}
        record AlreadyExists() implements Create {}
        record HandleDuplicate() implements Create {}
        record NicknameDuplicate() implements Create {}
    }

    sealed interface Get extends MemberProfileResult {
        record Success(String nickname) implements Get {}
        record NotFound() implements Get {}
    }

    sealed interface Update extends MemberProfileResult {
        record Success() implements Update {}
        record NotFound() implements Update {}
        record NicknameDuplicate() implements Update {}
    }

    sealed interface SoftDelete extends MemberProfileResult {
        record Success() implements SoftDelete {}
    }

    sealed interface HardDelete extends MemberProfileResult {
        record Success() implements HardDelete {}
    }

    // 나중에 정책 추가 가능 (ex: 금칙어)
    sealed interface CheckNickname extends MemberProfileResult {
        record Available(String message) implements CheckNickname {}
        record Unavailable(String message) implements CheckNickname {}
    }
}
