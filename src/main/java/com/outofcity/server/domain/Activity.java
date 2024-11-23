package com.outofcity.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Activity
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_member_id", nullable = false)
    private BusinessMember businessMember;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String state;

    @Column
    private Integer price;

    @Column
    private String mainCategory;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Builder
    public Activity(BusinessMember businessMember, String name, String description, String state, Integer price, String mainCategory, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude) {
        this.businessMember = businessMember;
        this.name = name;
        this.description = description;
        this.state = state;
        this.price = price;
        this.mainCategory = mainCategory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Activity of(BusinessMember businessMember, String name, String description, String state, Integer price, String mainCategory, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude) {
        return Activity.builder()
                .businessMember(businessMember)
                .name(name)
                .description(description)
                .state(state)
                .price(price)
                .mainCategory(mainCategory)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void AdminUpdateActivity(String name, String description, String state, Integer price, String mainCategory, String address, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.price = price;
        this.mainCategory = mainCategory;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}