package com.outofcity.server.dto.review.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponseDto(
    Integer location,
    Integer price,
    Integer interest,
    Integer service,
    String content,
    Double rating,
    LocalDateTime createdAt
) {
}
