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
@Table(name = "sub_category")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column
    private String name;

    @Builder
    public SubCategory(String name) {
        this.name = name;
    }

    public static SubCategory of(String name) {
        return SubCategory.builder()
                .name(name)
                .build();
    }

    public void updateSubCategory(String name) {
        this.name = name;
    }
}

