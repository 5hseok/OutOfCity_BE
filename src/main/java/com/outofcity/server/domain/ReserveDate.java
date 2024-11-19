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

    @OneToMany(mappedBy = "reserveDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReserveTime> reserveTimes;

    @Column(nullable = false)
    private LocalDateTime reserveDate;

    @Builder
    public ReserveDate(Activity activity, LocalDateTime reserveDate, List<ReserveTime> reserveTimes) {
        this.activity = activity;
        this.reserveDate = reserveDate;
        this.reserveTimes = reserveTimes;
    }
    public static ReserveDate of(Activity activity, LocalDateTime reserveDate, List<ReserveTime> reserveTimes) {
        return ReserveDate.builder()
                .activity(activity)
                .reserveDate(reserveDate)
                .reserveTimes(reserveTimes)
                .build();
    }
}