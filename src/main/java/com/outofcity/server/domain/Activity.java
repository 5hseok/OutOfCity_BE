package com.outofcity.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private String activityPhoto;

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

    @Column(precision = 9, scale = 6)
    private Double latitude;

    @Column(precision = 9, scale = 6)
    private Double longitude;

    @ManyToMany
    @JoinTable(
            name = "activity_sub_category",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    private List<SubCategory> subCategories;

    @ManyToMany
    @JoinTable(
            name = "activity_type",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<Type> types;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReserveDate> reserveDates;

    @Builder
    public Activity(BusinessMember businessMember, String name, String activityPhoto, String description, String state, Integer price, String mainCategory, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude, List<SubCategory> subCategories, List<Type> types, List<ReserveDate> reserveDates) {
        this.businessMember = businessMember;
        this.name = name;
        this.activityPhoto = activityPhoto;
        this.description = description;
        this.state = state;
        this.price = price;
        this.mainCategory = mainCategory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subCategories = subCategories;
        this.types = types;
        this.reserveDates = reserveDates;
    }

    public static Activity of(BusinessMember businessMember, String name, String activityPhoto, String description, String state, Integer price, String mainCategory, LocalDateTime createdAt, LocalDateTime updatedAt, String address, Double latitude, Double longitude, List<SubCategory> subCategories, List<Type> types, List<ReserveDate> reserveDates) {
        return Activity.builder()
                .businessMember(businessMember)
                .name(name)
                .activityPhoto(activityPhoto)
                .description(description)
                .state(state)
                .price(price)
                .mainCategory(mainCategory)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .subCategories(subCategories)
                .types(types)
                .reserveDates(reserveDates)
                .build();
    }

    public void AdminUpdateActivity(String name, String activityPhoto, String description, String state, Integer price, String mainCategory, String address, Double latitude, Double longitude, List<SubCategory> subCategories, List<Type> types, List<ReserveDate> reserveDates) {
        this.name = name;
        this.activityPhoto = activityPhoto;
        this.description = description;
        this.state = state;
        this.price = price;
        this.mainCategory = mainCategory;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subCategories = subCategories;
        this.types = types;
        this.reserveDates = reserveDates;
    }
}