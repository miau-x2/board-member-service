package com.example.board.member.profile.service;

import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.command.MemberProfileUpdateCommand;
import com.example.board.member.profile.service.result.MemberProfileResult;

public interface MemberProfileService {
    MemberProfileResult.Create createProfileIfNotExists(Long id, MemberProfileCreateCommand command);
    MemberProfileResult.CheckNickname checkNicknameAvailability(String nickname);
    MemberProfileResult.Get getProfile(String handle);
    MemberProfileResult.Update updateProfile(Long id, MemberProfileUpdateCommand command);
}
