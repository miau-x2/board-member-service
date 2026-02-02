package com.example.board.member.profile.entity;

import com.example.board.member.commons.utils.DatabaseConstraintName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "member_profile",
        uniqueConstraints = {
        @UniqueConstraint(name = DatabaseConstraintName.MemberProfile.HANDLE, columnNames = "handle"),
        @UniqueConstraint(name = DatabaseConstraintName.MemberProfile.NICKNAME, columnNames = "nickname")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member_profile SET withdrawn_at = NOW() WHERE member_id = ?")
@SQLRestriction("withdrawn_at IS NULL")
public class MemberProfile extends BaseEntity implements Persistable<Long> {
    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(name = "handle", nullable = false, unique = true)
    private String handle;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        this.isNew = false;
    }

    private MemberProfile(Long id, String handle, String nickname) {
        this.id = id;
        this.handle = handle;
        this.nickname = nickname;
    }

    public static MemberProfile create(Long id, String handle, String nickname) {
        return new MemberProfile(id, handle, nickname);
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }
}
