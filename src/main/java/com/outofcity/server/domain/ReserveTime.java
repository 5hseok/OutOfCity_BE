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
@Table(name = "reserve_time")
public class ReserveTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_time_id")
    private Long reserveTimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_date_id", nullable = false)
    private ReserveDate reserveDate;

    @Column(nullable = false)
    private LocalDateTime reserveTime;

    @Builder
    public ReserveTime(ReserveDate reserveDate, LocalDateTime reserveTime) {
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
    }

    public static ReserveTime of(ReserveDate reserveDate, LocalDateTime reserveTime) {
        return ReserveTime.builder()
                .reserveDate(reserveDate)
                .reserveTime(reserveTime)
                .build();
    }
}