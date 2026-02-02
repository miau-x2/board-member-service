package com.example.board.member.profile.service;

import com.example.board.member.commons.utils.DatabaseConstraintName;
import com.example.board.member.profile.repository.MemberProfileRepository;
import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.impl.MemberProfileServiceImpl;
import com.example.board.member.profile.service.result.MemberProfileResult;
import com.example.board.member.profile.tx.MemberProfileTxWriter;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberProfileServiceTest {
    @Mock
    private MemberProfileRepository memberProfileRepository;
    @Mock
    private MemberProfileTxWriter memberProfileTxWriter;
    @InjectMocks
    private MemberProfileServiceImpl memberProfileService;

    @Test
    @DisplayName("회원 프로필 생성 - 성공")
    void createProfileIfNotExists_success() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(false);
        when(memberProfileRepository.existsByNickname(command.nickname())).thenReturn(false);
        doNothing().when(memberProfileTxWriter).save(id, command);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.Success.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository).existsByNickname(command.nickname());
        verify(memberProfileTxWriter).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 성공(이미 존재하는 경우)")
    void createProfileIfNotExists_success_when_already_exists() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        when(memberProfileRepository.existsById(id)).thenReturn(true);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.AlreadyExists.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository, never()).existsByHandle(command.handle());
        verify(memberProfileRepository, never()).existsByNickname(command.nickname());
        verify(memberProfileTxWriter, never()).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 성공(이미 존재하는 경우 - pk 제약조건 예외)")
    void createProfileIfNotExists_success_when_already_exists_throws_pk_constraint() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        var exception = createDataIntegrityViolationException(DatabaseConstraintName.MemberProfile.PK);
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(false);
        when(memberProfileRepository.existsByNickname(command.nickname())).thenReturn(false);
        doThrow(exception).when(memberProfileTxWriter).save(id, command);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.AlreadyExists.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository).existsByNickname(command.nickname());
        verify(memberProfileTxWriter).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 실패(핸들 중복)")
    void createProfileIfNotExists_fail_when_handle_duplicate() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(true);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.HandleDuplicate.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository, never()).existsByNickname(command.nickname());
        verify(memberProfileTxWriter, never()).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 실패(핸들 중복 - 핸들 유니크 제약조건 예외)")
    void createProfileIfNotExists_fail_when_handle_duplicate_throws_handle_constraint() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        var exception = createDataIntegrityViolationException(DatabaseConstraintName.MemberProfile.HANDLE);
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(false);
        when(memberProfileRepository.existsByNickname(command.nickname())).thenReturn(false);
        doThrow(exception).when(memberProfileTxWriter).save(id, command);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.HandleDuplicate.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository).existsByNickname(command.nickname());
        verify(memberProfileTxWriter).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 실패(닉네임 중복)")
    void createProfileIfNotExists_fail_when_nickname_duplicate() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(false);
        when(memberProfileRepository.existsByNickname(command.nickname())).thenReturn(true);

        var actual = memberProfileService.createProfileIfNotExists(id ,command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.NicknameDuplicate.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository).existsByNickname(command.nickname());
        verify(memberProfileTxWriter, never()).save(id, command);
    }

    @Test
    @DisplayName("회원 프로필 생성 - 실패(닉네임 중복 - 닉네임 유니크 제약조건 예외)")
    void createProfileIfNotExists_fail_when_nickname_duplicate_throws_nickname_constraint() {
        var id = 1L;
        var command = new MemberProfileCreateCommand("test", "testuser");
        var exception = createDataIntegrityViolationException(DatabaseConstraintName.MemberProfile.NICKNAME);
        when(memberProfileRepository.existsById(id)).thenReturn(false);
        when(memberProfileRepository.existsByHandle(command.handle())).thenReturn(false);
        when(memberProfileRepository.existsByNickname(command.nickname())).thenReturn(false);
        doThrow(exception).when(memberProfileTxWriter).save(id, command);

        var actual = memberProfileService.createProfileIfNotExists(id, command);
        assertThat(actual).isInstanceOf(MemberProfileResult.Create.NicknameDuplicate.class);

        verify(memberProfileRepository).existsById(id);
        verify(memberProfileRepository).existsByHandle(command.handle());
        verify(memberProfileRepository).existsByNickname(command.nickname());
        verify(memberProfileTxWriter).save(id, command);
    }

    private DataIntegrityViolationException createDataIntegrityViolationException(String constraintName) {
        var cve = new ConstraintViolationException("msg", null, constraintName);
        return new DataIntegrityViolationException("msg", cve);
    }
}