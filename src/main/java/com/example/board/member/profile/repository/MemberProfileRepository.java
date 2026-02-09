package com.example.board.member.profile.repository;

import com.example.board.member.profile.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    boolean existsByHandle(String handle);
    boolean existsByNickname(String nickname);
    boolean existsByNicknameAndIdNot(String nickname, Long id);
    Optional<MemberProfile> findByHandle(String handle);

    @Modifying
    @Query(value = "DELETE FROM member_profile WHERE member_id = :memberId", nativeQuery = true)
    void physicalDeleteById(@Param("memberId") Long id);
}
