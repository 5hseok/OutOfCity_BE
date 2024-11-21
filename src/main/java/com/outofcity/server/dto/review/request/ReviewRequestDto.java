package com.outofcity.server.dto.review.request;

import lombok.Builder;

public record ReviewRequestDto(
    Integer location,
    Integer service,
    Integer interest,
    Integer price,
    String content
) {
}
