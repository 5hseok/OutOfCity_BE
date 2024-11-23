package com.outofcity.server.controller.activity;

import com.outofcity.server.dto.activity.request.ActivityCartRequestDto;
import com.outofcity.server.dto.activity.response.ActivityCartResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.activity.ActivityCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities/cart")
public class ActivityCartController {

    private final ActivityCartService activityCartService;

    // 장바구니 항목 조회
    @GetMapping("")
    public ResponseEntity<SuccessStatusResponse<List<ActivityCartResponseDto>>> getCartItems(
            @RequestHeader("Authorization") String token) {
        List<ActivityCartResponseDto> responseDtos = activityCartService.getCartItems(token);
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.CART_ITEM_LIST_SUCCESS, responseDtos
        ));
    }

    // 장바구니에 항목 추가
    @PostMapping("/{activityId}")
    public ResponseEntity<SuccessStatusResponse<ActivityCartResponseDto>> addToCart(
            @RequestHeader("Authorization") String token, @PathVariable Long activityId, @RequestBody ActivityCartRequestDto activityCartRequestDto) {
        ActivityCartResponseDto activityCartResponseDto = activityCartService.addToCart(token, activityId, activityCartRequestDto);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.CART_ITEM_ADD_SUCCESS, activityCartResponseDto));
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<SuccessStatusResponse<SuccessMessage>> deleteCart(
            @RequestHeader("Authorization") String token, @PathVariable Long cartId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.CART_DELETE_SUCCESS, activityCartService.deleteCart(token, cartId)
        ));
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<SuccessStatusResponse<SuccessMessage>> deleteCartItem(
            @RequestHeader("Authorization") String token, @PathVariable Long cartItemId) {
        return ResponseEntity.ok(SuccessStatusResponse.of(
                SuccessMessage.CART_ITEM_DELETE_SUCCESS, activityCartService.deleteCartItem(token, cartItemId)
        ));
    }
}
