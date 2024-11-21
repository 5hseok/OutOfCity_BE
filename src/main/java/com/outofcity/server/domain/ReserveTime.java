package com.outofcity.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.sql.Time;
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
    private Time reserveTime;

    @Builder
    public ReserveTime(ReserveDate reserveDate, Time reserveTime) {
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
    }

    public static ReserveTime of(ReserveDate reserveDate, Time reserveTime) {
        return ReserveTime.builder()
                .reserveDate(reserveDate)
                .reserveTime(reserveTime)
                .build();
    }
}