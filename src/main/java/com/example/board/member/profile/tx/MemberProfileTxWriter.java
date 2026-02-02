package com.example.board.member.profile.tx;

import com.example.board.member.profile.entity.MemberProfile;
import com.example.board.member.profile.exceptions.MemberProfileNotFoundException;
import com.example.board.member.profile.repository.MemberProfileRepository;
import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.command.MemberProfileUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberProfileTxWriter {
    private final MemberProfileRepository memberProfileRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Long id, MemberProfileCreateCommand command) {
        var profile = MemberProfile.create(id, command.handle(), command.nickname());
        memberProfileRepository.saveAndFlush(profile);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(Long id, MemberProfileUpdateCommand command) {
        var profile = memberProfileRepository
                .findById(id)
                .orElseThrow(() -> new MemberProfileNotFoundException("존재하지 않는 프로필입니다."));
        profile.update(command.nickname());
        memberProfileRepository.flush();
    }
}
