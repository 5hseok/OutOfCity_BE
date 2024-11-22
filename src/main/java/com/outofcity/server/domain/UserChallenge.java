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

    @Column(length = 20, nullable = false)
    private String certification;

    @Column(nullable = false)
    private LocalDate performedAt;

    @Builder
    public UserChallenge(GeneralMember generalMember, Challenge challenge, String imageUrl, String certification, LocalDate performedAt) {
        this.generalMember = generalMember;
        this.challenge = challenge;
        this.imageUrl = imageUrl;
        this.certification = certification;
        this.performedAt = performedAt;
    }

    public static UserChallenge of(GeneralMember generalMember, Challenge challenge, String imageUrl, String certification, LocalDate performedAt) {
        return UserChallenge.builder()
                .generalMember(generalMember)
                .challenge(challenge)
                .imageUrl(imageUrl)
                .certification(certification)
                .performedAt(performedAt)
                .build();
    }

    public void userUpdateUserChallenge(String imageUrl, String certification) {
        this.imageUrl = imageUrl;
    }

    public void adminUpdateUserChallenge(String imageUrl, String certification) {
        this.certification = certification;
    }
}