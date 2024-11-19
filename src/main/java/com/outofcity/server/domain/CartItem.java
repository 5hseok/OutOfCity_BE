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
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_time_id", nullable = false)
    private ReserveTime reserveTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_date_id", nullable = false)
    private ReserveDate reserveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_participants_id", nullable = false)
    private ReserveParticipants reserveParticipants;

    @Builder
    public CartItem(Cart cart, Activity activity, ReserveTime reserveTime, ReserveDate reserveDate, ReserveParticipants reserveParticipants) {
        this.cart = cart;
        this.activity = activity;
        this.reserveTime = reserveTime;
        this.reserveDate = reserveDate;
        this.reserveParticipants = reserveParticipants;
    }

    public static CartItem of(Cart cart, Activity activity, ReserveTime reserveTime, ReserveDate reserveDate, ReserveParticipants reserveParticipants) {
        return CartItem.builder()
                .cart(cart)
                .activity(activity)
                .reserveTime(reserveTime)
                .reserveDate(reserveDate)
                .reserveParticipants(reserveParticipants)
                .build();
    }
}
