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
@Table(name = "reserve")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Long reserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_member_id", nullable = false)
    private GeneralMember generalMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_time_id", nullable = false)
    private ReserveTime reserveTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_date_id", nullable = false)
    private ReserveDate reserveDate;

    @Column(nullable = false)
    private LocalDateTime reservedAt;

    @Column(nullable = false)
    private Integer reservedParticipants;

    @Column(nullable = false, length = 20)
    private String reserveState;

    @Builder
    public Reserve(Activity activity, GeneralMember generalMember, ReserveTime reserveTime, ReserveDate reserveDate, LocalDateTime reservedAt, Integer reservedParticipants, String reserveState) {
        this.activity = activity;
        this.generalMember = generalMember;
        this.reserveTime = reserveTime;
        this.reserveDate = reserveDate;
        this.reservedAt = reservedAt;
        this.reservedParticipants = reservedParticipants;
        this.reserveState = reserveState;
    }

    public static Reserve of(Activity activity, GeneralMember generalMember, ReserveTime reserveTime, ReserveDate reserveDate, LocalDateTime reservedAt, Integer reservedParticipants, String reserveState) {
        return Reserve.builder()
                .activity(activity)
                .generalMember(generalMember)
                .reserveTime(reserveTime)
                .reserveDate(reserveDate)
                .reservedAt(reservedAt)
                .reservedParticipants(reservedParticipants)
                .reserveState(reserveState)
                .build();
    }
}