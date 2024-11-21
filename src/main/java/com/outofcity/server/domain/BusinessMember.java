package com.outofcity.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "business_member")
public class BusinessMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_member_id")
    private Long businessMemberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long businessNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private LocalDate startDate;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Builder
    public BusinessMember(String name, Long businessNumber, String address, LocalDate startDate, String phoneNumber, String email) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.address = address;
        this.startDate = startDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public static BusinessMember of(String name, Long businessNumber, String address, LocalDate startDate, String phoneNumber, String email) {
        return BusinessMember.builder()
                .name(name)
                .businessNumber(businessNumber)
                .address(address)
                .startDate(startDate)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
    }

    public void businessUpdateBusinessMember(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}