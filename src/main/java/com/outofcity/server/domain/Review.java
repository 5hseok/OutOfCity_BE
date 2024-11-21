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
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_member_id", nullable = false)
    private GeneralMember generalMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_member_id", nullable = false)
    private BusinessMember businessMember;

    @Column(nullable = false)
    private Integer location;

    @Column(nullable = false)
    private Integer service;

    @Column(nullable = false)
    private Integer interest;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "bigint default 0")
    private Long likes;

    @Column()
    private LocalDateTime createdAt;

    @Builder
    public Review(Activity activity, GeneralMember generalMember, BusinessMember businessMember, Integer location, Integer service, Integer interest, Integer price, Double rating, String content, Long likes, LocalDateTime createdAt) {
        this.activity = activity;
        this.generalMember = generalMember;
        this.businessMember = businessMember;
        this.location = location;
        this.service = service;
        this.interest = interest;
        this.price = price;
        this.rating = rating;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public static Review of(Activity activity, GeneralMember generalMember, BusinessMember businessMember, Integer location, Integer service, Integer interest, Integer price, Double rating, String content, Long likes, LocalDateTime createdAt) {
        return Review.builder()
                .activity(activity)
                .generalMember(generalMember)
                .businessMember(businessMember)
                .location(location)
                .service(service)
                .interest(interest)
                .price(price)
                .rating(rating)
                .content(content)
                .likes(likes)
                .createdAt(createdAt)
                .build();
    }

//    public void updateReview(String content) {
//        this.content = content;
//    }
}
