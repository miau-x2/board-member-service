package com.example.board.member.commons.utils;

public final class DatabaseConstraintName {
    private DatabaseConstraintName() {}

    public static final class MemberProfile {
        public static final String PK = "member_profile.PRIMARY";
        public static final String HANDLE = "member_profile.uk_member_profile_handle";
        public static final String NICKNAME = "member_profile.uk_member_profile_nickname";
    }
}
