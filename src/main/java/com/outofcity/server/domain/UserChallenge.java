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
@Table(name = "user_challenge")
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_challenge_id")
    private Long userChallengeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_member_id", nullable = false)
    private GeneralMember generalMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 10, nullable = false, columnDefinition = "varchar(10) default '미인증'")
    private String certification;

    @Builder
    public UserChallenge(GeneralMember generalMember, Challenge challenge, String imageUrl, String certification) {
        this.generalMember = generalMember;
        this.challenge = challenge;
        this.imageUrl = imageUrl;
        this.certification = certification;
    }

    public static UserChallenge of(GeneralMember generalMember, Challenge challenge, String imageUrl, String certification) {
        return UserChallenge.builder()
                .generalMember(generalMember)
                .challenge(challenge)
                .imageUrl(imageUrl)
                .certification(certification)
                .build();
    }

    public void userUpdateUserChallenge(String imageUrl, String certification) {
        this.imageUrl = imageUrl;
    }

    public void adminUpdateUserChallenge(String imageUrl, String certification) {
        this.certification = certification;
    }
}