package com.outofcity.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "activity_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Builder
    public ActivityImage(String imageUrl, Activity activity) {
        this.imageUrl = imageUrl;
        this.activity = activity;
    }
}
