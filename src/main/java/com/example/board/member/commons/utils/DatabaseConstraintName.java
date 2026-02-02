package com.example.board.member.commons.utils;

public final class DatabaseConstraintName {
    private DatabaseConstraintName() {}

    public static final class MemberProfile {
        public static final String PK = "PRIMARY";
        public static final String HANDLE = "uk_member_profile_handle";
        public static final String NICKNAME = "uk_member_profile_nickname";
    }
}
