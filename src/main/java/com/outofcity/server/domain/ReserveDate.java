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
@Table(name = "reserve_date")
public class ReserveDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_date_id")
    private Long reserveDateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(nullable = false)
    private LocalDate reserveDate;

    @Builder
    public ReserveDate(Activity activity, LocalDate reserveDate) {
        this.activity = activity;
        this.reserveDate = reserveDate;
    }
    public static ReserveDate of(Activity activity, LocalDate reserveDate) {
        return ReserveDate.builder()
                .activity(activity)
                .reserveDate(reserveDate)
                .build();
    }
}