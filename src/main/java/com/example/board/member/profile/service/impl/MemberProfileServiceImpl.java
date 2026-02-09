package com.example.board.member.profile.service.impl;

import com.example.board.member.commons.exception.UnhandledDataIntegrityViolationException;
import com.example.board.member.commons.utils.DatabaseConstraintName;
import com.example.board.member.commons.utils.ExceptionsUtils;
import com.example.board.member.profile.entity.MemberProfile;
import com.example.board.member.profile.exceptions.MemberProfileNotFoundException;
import com.example.board.member.profile.repository.MemberProfileRepository;
import com.example.board.member.profile.service.MemberProfileService;
import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.command.MemberProfileUpdateCommand;
import com.example.board.member.profile.service.result.MemberProfileResult;
import com.example.board.member.profile.tx.MemberProfileTxWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {
    private final MemberProfileRepository memberProfileRepository;
    private final MemberProfileTxWriter memberProfileTxWriter;

    @Override
    public MemberProfileResult.Create createProfileIfNotExists(Long id, MemberProfileCreateCommand command) {
        if(memberProfileRepository.existsById(id)) {
            return new MemberProfileResult.Create.AlreadyExists();
        }
        if(memberProfileRepository.existsByHandle(command.handle())) {
            return new MemberProfileResult.Create.HandleDuplicate();
        }
        if(memberProfileRepository.existsByNickname(command.nickname())) {
            return new MemberProfileResult.Create.NicknameDuplicate();
        }
        try {
            memberProfileTxWriter.save(id, command);
            log.info("회원 프로필 생성: {} {}", command.handle(), command.nickname());
            return new MemberProfileResult.Create.Success();
        } catch (DataIntegrityViolationException e) {
            var constraintName = ExceptionsUtils.findConstraintName(e);
            if(DatabaseConstraintName.MemberProfile.PK.equals(constraintName)) {
                return new MemberProfileResult.Create.AlreadyExists();
            }
            if(DatabaseConstraintName.MemberProfile.HANDLE.equals(constraintName)) {
                return new MemberProfileResult.Create.HandleDuplicate();
            }
            if(DatabaseConstraintName.MemberProfile.NICKNAME.equals(constraintName)) {
                return new MemberProfileResult.Create.NicknameDuplicate();
            }
            throw new UnhandledDataIntegrityViolationException(e);
        }
    }

    @Override
    public MemberProfileResult.CheckNickname checkNicknameAvailability(String nickname) {
        if(memberProfileRepository.existsByNickname(nickname)) {
            return new MemberProfileResult.CheckNickname.Unavailable("이미 사용 중인 닉네임입니다.");
        }
        return new MemberProfileResult.CheckNickname.Available("사용 가능한 닉네임입니다.");
    }

    @Override
    public MemberProfileResult.Get getProfile(String handle) {
        return memberProfileRepository.findByHandle(handle)
                .map(MemberProfile::getNickname)
                .<MemberProfileResult.Get>map(MemberProfileResult.Get.Success::new)
                .orElseGet(MemberProfileResult.Get.NotFound::new);
    }

    @Override
    public MemberProfileResult.Update updateProfile(Long id, MemberProfileUpdateCommand command) {
        if(memberProfileRepository.existsByNicknameAndIdNot(command.nickname(), id)) {
            return new MemberProfileResult.Update.NicknameDuplicate();
        }
        try {
            memberProfileTxWriter.update(id, command);
            log.info("회원 프로필 수정: {}", command.nickname());
            return new MemberProfileResult.Update.Success();
        } catch (MemberProfileNotFoundException _) {
            return new MemberProfileResult.Update.NotFound();
        } catch (DataIntegrityViolationException e) {
            var constraintName = ExceptionsUtils.findConstraintName(e);
            if(DatabaseConstraintName.MemberProfile.NICKNAME.equals(constraintName)) {
                return new MemberProfileResult.Update.NicknameDuplicate();
            }
            throw new UnhandledDataIntegrityViolationException(e);
        }
    }

    @Override
    public MemberProfileResult.SoftDelete softDeleteProfile(Long id) {
        memberProfileRepository.deleteById(id);
        log.info("[SOFT] 회원 프로필 삭제: {}", id);
        return new MemberProfileResult.SoftDelete.Success();
    }

    @Override
    @Transactional
    public MemberProfileResult.HardDelete hardDeleteProfile(Long id) {
        memberProfileRepository.physicalDeleteById(id);
        log.info("[HARD] 회원 프로필 삭제: {}", id);
        return new MemberProfileResult.HardDelete.Success();
    }
}
