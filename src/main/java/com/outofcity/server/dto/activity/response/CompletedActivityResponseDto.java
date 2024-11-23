package com.outofcity.server.dto.activity.response;

import java.util.List;

public record CompletedActivityResponseDto(
        String orderName,
        String orderTime,
        Integer orderPerson,
        List<String> orderActivityPhoto,
        Boolean reviewExist,
        List<Review> reviews // 리뷰를 리스트 형태로 수정
) {
    public record Review(
            Long id,
            Writer writer,
            Integer reviewLocation,
            Integer reviewService,
            Integer reviewInterest,
            Integer reviewPrice,
            Double reviewTotalStar,
            String reviewContent,
            Long reviewLikeCount,
            String createdAt
    ) {
        public record Writer(
                Long id,
                String name
        ) {
        }
    }
}
