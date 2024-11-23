package com.outofcity.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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
    private LocalTime reserveTime;

    @Builder
    public ReserveTime(ReserveDate reserveDate, LocalTime reserveTime) {
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
    }

    public static ReserveTime of(ReserveDate reserveDate, LocalTime reserveTime) {
        return ReserveTime.builder()
                .reserveDate(reserveDate)
                .reserveTime(reserveTime)
                .build();
    }
}