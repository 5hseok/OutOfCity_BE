package com.outofcity.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "general_member")
public class GeneralMember {

    @Id
    @Column(name = "general_member_id")
    private Long generalMemberId;

    @Column(nullable = false)
    private String name;

    @Column
    private String nickname;

    @Column(name = "`rank`", columnDefinition = "varchar(20) default '씨앗'")
    private String rank;

    @Column
    private String profileImageUrl;

    @Column
    private String email;

    @Builder
    public GeneralMember(Long id, String name, String rank, String profileImageUrl, String email) {
        this.generalMemberId = id;
        this.name = name;
        this.nickname = name;
        this.rank = rank;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }

    public GeneralMember of(Long id, String name, String rank, String profileImageUrl, String email) {
        return GeneralMember.builder()
                .id(id)
                .name(name)
                .rank(rank)
                .profileImageUrl(profileImageUrl)
                .email(email)
                .build();
    }

    public void userUpdateGeneralMember(String nickname) {
        this.nickname = nickname;
    }

    public void adminUpdateGeneralMember(String rank) {
        this.rank = rank;
    }
}
