package com.example.board.member.profile.repository;

import com.example.board.member.profile.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    boolean existsByHandle(String handle);
    boolean existsByNickname(String nickname);
    boolean existsByNicknameAndIdNot(String nickname, Long id);
    Optional<MemberProfile> findByHandle(String handle);
}
