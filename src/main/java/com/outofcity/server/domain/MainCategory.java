package com.outofcity.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MainCategory {
    MOUNTAINS(1, "산으로"),
    SEA(2, "바다로"),
    FARM(3, "농촌으로");

    private final int id;
    private final String description;
}

