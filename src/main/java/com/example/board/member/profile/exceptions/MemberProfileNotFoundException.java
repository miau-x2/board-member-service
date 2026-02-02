package com.example.board.member.profile.exceptions;

public class MemberProfileNotFoundException extends RuntimeException {
    public MemberProfileNotFoundException(String message) {
        super(message);
    }
}
