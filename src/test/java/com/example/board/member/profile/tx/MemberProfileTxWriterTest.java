package com.example.board.member.profile.tx;

import com.example.board.member.profile.entity.MemberProfile;
import com.example.board.member.profile.exceptions.MemberProfileNotFoundException;
import com.example.board.member.profile.repository.MemberProfileRepository;
import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.command.MemberProfileUpdateCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
@Import(MemberProfileTxWriter.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MemberProfileTxWriterTest {
    @Autowired
    private MemberProfileRepository memberProfileRepository;
    @Autowired
    private MemberProfileTxWriter memberProfileTxWriter;

    @AfterEach
    void cleanUp() {
        memberProfileRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 프로필 저장 - 실패(id 중복)")
    void save_fail_when_duplicate_id() {
        var id = 1L;
        var command1 = new MemberProfileCreateCommand("test1", "testuser1");
        var command2 = new MemberProfileCreateCommand("test2", "testuser2");
        memberProfileRepository.saveAndFlush(MemberProfile.create(id, command1.handle(), command1.nickname()));

        assertThatThrownBy(() -> memberProfileTxWriter.save(id, command2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원 프로필 저장 - 실패(핸들 중복)")
    void save_fail_when_duplicate_handle() {
        var id1 = 1L;
        var command1 = new MemberProfileCreateCommand("test1", "testuser1");
        var id2 = 2L;
        var command2 = new MemberProfileCreateCommand("test1", "testuser2");
        memberProfileRepository.saveAndFlush(MemberProfile.create(id1, command1.handle(), command1.nickname()));

        assertThatThrownBy(() -> memberProfileTxWriter.save(id2, command2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원 프로필 저장 - 실패(닉네임 중복)")
    void save_fail_when_duplicate_nickname() {
        var id1 = 1L;
        var command1 = new MemberProfileCreateCommand("test1", "testuser1");
        var id2 = 2L;
        var command2 = new MemberProfileCreateCommand("test2", "testuser1");
        memberProfileRepository.saveAndFlush(MemberProfile.create(id1, command1.handle(), command1.nickname()));

        assertThatThrownBy(() -> memberProfileTxWriter.save(id2, command2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원 프로필 수정 - 실패(존재하지 않는 프로필)")
    void update_fail_when_not_found() {
        var id = 1L;
        var command = new MemberProfileUpdateCommand("닉네임");

        assertThatThrownBy(() -> memberProfileTxWriter.update(id, command))
                .isExactlyInstanceOf(MemberProfileNotFoundException.class);
    }

    @Test
    @DisplayName("회원 프로필 수정 - 실패(닉네임 중복)")
    void update_fail_when_duplicate_nickname() {
        var id = 10L;
        var command = new MemberProfileUpdateCommand("닉네임1");
        memberProfileRepository.saveAndFlush(MemberProfile.create(1L, "user1", "닉네임1"));
        memberProfileRepository.saveAndFlush(MemberProfile.create(10L, "user10", "닉네임10"));

        assertThatThrownBy(() -> memberProfileTxWriter.update(id, command))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}