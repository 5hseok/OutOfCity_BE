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
@Table(name = "challenge")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long challengeId;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public Challenge(String content, LocalDateTime createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Challenge of (String content, LocalDateTime createdAt) {
        return Challenge.builder()
                .content(content)
                .createdAt(createdAt)
                .build();
    }

    public void adminUpdateChallenge(String content) {
        this.content = content;
    }
}