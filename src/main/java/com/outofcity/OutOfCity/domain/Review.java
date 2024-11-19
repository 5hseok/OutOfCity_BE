package com.outofcity.OutOfCity.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
// Review Entity
@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "general_member_id", nullable = false)
    private Long generalMemberId;

    @Column(name = "business_member_id", nullable = false)
    private Long businessMemberId;

    @Column(name = "location", nullable = false)
    private Integer location;

    @Column(name = "service", nullable = false)
    private Integer service;

    @Column(name = "interest", nullable = false)
    private Integer interest;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @Column(name = "likes", columnDefinition = "bigint default 0")
    private Long likes;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Builder
    public Review(Long reviewId, Long activityId, Long generalMemberId, Long businessMemberId, Integer location, Integer service, Integer interest, Integer price, Double rating, String content, Long likes, LocalDate createdAt) {
        this.reviewId = reviewId;
        this.activityId = activityId;
        this.generalMemberId = generalMemberId;
        this.businessMemberId = businessMemberId;
        this.location = location;
        this.service = service;
        this.interest = interest;
        this.price = price;
        this.rating = rating;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }
}
