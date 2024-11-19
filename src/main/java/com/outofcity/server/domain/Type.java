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
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long typeId;

    @Column(length = 64, nullable = false)
    private String name;

    @Builder
    public Type(String name) {
        this.name = name;
    }

    public static Type of(String name) {
        return Type.builder()
                .name(name)
                .build();
    }

    public void updateType(String name) {
        this.name = name;
    }
}
