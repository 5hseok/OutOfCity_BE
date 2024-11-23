package com.outofcity.server.service.activity;

import com.outofcity.server.domain.*;
import com.outofcity.server.dto.activity.request.ActivityCartRequestDto;
import com.outofcity.server.dto.activity.response.ActivityCartResponseDto;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import com.outofcity.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityCartService {

    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralMemberRepository generalMemberRepository;
    private final ActivityRepository activityRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartDateRepository cartDateRepository;
    private final CartTimeRepository cartTimeRepository;
    private final CartParticipantsRepository cartParticipantsRepository;

    // 장바구니 항목 조회
    public List<ActivityCartResponseDto> getCartItems(String token) {
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByGeneralMember(generalMember)
                .orElseThrow(() -> new BusinessException(ErrorMessage.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        return cartItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 장바구니에 항목 추가
    public ActivityCartResponseDto addToCart(String token, Long activityId, ActivityCartRequestDto activityCartRequestDto) {
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 액티비티 조회
        log.info("Fetching activity for ID: {}", activityId);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.error("Activity not found for ID: {}", activityId);
                    return new BusinessException(ErrorMessage.ACTIVITY_NOT_FOUND);
                });

        // 예약 가능한 날짜 조회
        List<ReserveDate> reserveDates = cartDateRepository.findByActivity(activity);
        if (reserveDates == null || reserveDates.isEmpty()) {
            log.error("No reserve dates found for activity ID: {}", activityId);
            throw new BusinessException(ErrorMessage.INVALID_RESERVE_DATE);
        }

        // 예약 날짜 및 시간 선택
        ReserveDate selectedReserveDate = reserveDates.stream()
                .filter(date -> date.getReserveDate().equals(activityCartRequestDto.date()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Invalid reserve date provided: {}", activityCartRequestDto.date());
                    return new BusinessException(ErrorMessage.INVALID_RESERVE_DATE);
                });

        List<ReserveTime> reserveTimes = cartTimeRepository.findByReserveDate(selectedReserveDate);
        if (reserveTimes == null || reserveTimes.isEmpty()) {
            log.error("No reserve times found for reserve date: {}", selectedReserveDate.getReserveDate());
            throw new BusinessException(ErrorMessage.INVALID_RESERVE_TIME);
        }

        ReserveTime selectedReserveTime = reserveTimes.stream()
                .filter(time -> time.getReserveTime().equals(activityCartRequestDto.time()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Invalid reserve time provided: {}", activityCartRequestDto.time());
                    return new BusinessException(ErrorMessage.INVALID_RESERVE_TIME);
                });

        // 참가자 수 검증
        ReserveParticipants reservedParticipants = cartParticipantsRepository.findByReserveTime(selectedReserveTime)
                .orElseThrow(() -> {
                    log.error("Participants not found for reserve time: {}", selectedReserveTime.getReserveTime());
                    return new BusinessException(ErrorMessage.INVALID_PARTICIPANTS);
                });

        Integer maxParticipants = reservedParticipants.getMaxParticipants();
        if (activityCartRequestDto.participants() > maxParticipants) {
            log.error("Invalid participants count. Max: {}, Requested: {}", maxParticipants, activityCartRequestDto.participants());
            throw new BusinessException(ErrorMessage.INVALID_RESERVE_PARTICIPANTS);
        }

        // 장바구니 추가 로직
        Cart cart = cartRepository.findByGeneralMember(generalMember)
                .orElseGet(() -> cartRepository.save(Cart.of(generalMember)));

        CartItem cartItem = CartItem.of(cart, activity, selectedReserveTime, selectedReserveDate, reservedParticipants);
        cartItemRepository.save(cartItem);

        return ActivityCartResponseDto.of(
                200,
                "장바구니 항목 추가 성공",
                cartItem.getCartItemId(),
                activity.getActivityId(),
                generalMemberId,
                selectedReserveDate.getReserveDate(),
                selectedReserveTime.getReserveTime(),
                activityCartRequestDto.participants(),
                activity.getPrice(),
                cart.getCartId()
        );
    }

    // 장바구니 삭제
    public SuccessMessage deleteCart(String token, Long cartId) {
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 장바구니 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.CART_NOT_FOUND));

        // 장바구니가 해당 사용자의 것인지 확인
        if (!cart.getGeneralMember().equals(generalMember)) {
            log.error("Cart does not belong to member ID: {}. Cart Owner: {}", generalMemberId, cart.getGeneralMember().getGeneralMemberId());
            throw new BusinessException(ErrorMessage.CART_NOT_BELONG_TO_MEMBER);
        }

        // 장바구니와 연관 데이터 삭제
        try {
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            log.info("Deleting {} CartItems associated with the cart", cartItems.size());
            if (!cartItems.isEmpty()) {
                cartItemRepository.deleteAll(cartItems);
            }

            log.info("Deleting cart ID: {}", cartId);
            cartRepository.delete(cart);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while deleting cart ID: {}. Error: {}", cartId, e.getMessage(), e);
            throw new BusinessException(ErrorMessage.INVALID_DATA);
        } catch (Exception e) {
            log.error("Error while deleting cart ID: {}. Error: {}", cartId, e.getMessage(), e);
            throw new BusinessException(ErrorMessage.DATABASE_ERROR);
        }

        return SuccessMessage.CART_DELETE_SUCCESS;
    }

    // 장바구니 항목 삭제
    public SuccessMessage deleteCartItem(String token, Long cartItemId) {
        Long generalMemberId = jwtTokenProvider.getUserFromJwt(token);
        GeneralMember generalMember = generalMemberRepository.findById(generalMemberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GENERAL_MEMBER_NOT_FOUND));

        // 장바구니 항목 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.CART_ITEM_NOT_FOUND));

        // 장바구니 항목이 해당 사용자의 것인지 확인
        if (!cartItem.getCart().getGeneralMember().equals(generalMember)) {
            log.error("Cart item does not belong to member ID: {}. Cart Owner: {}", generalMemberId, cartItem.getCart().getGeneralMember().getGeneralMemberId());
            throw new BusinessException(ErrorMessage.CART_NOT_BELONG_TO_MEMBER);
        }

        // 장바구니 항목 삭제
        try {
            log.info("Deleting cart item ID: {}", cartItemId);
            cartItemRepository.delete(cartItem);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while deleting cart item ID: {}. Error: {}", cartItemId, e.getMessage(), e);
            throw new BusinessException(ErrorMessage.INVALID_DATA);
        } catch (Exception e) {
            log.error("Error while deleting cart item ID: {}. Error: {}", cartItemId, e.getMessage(), e);
            throw new BusinessException(ErrorMessage.DATABASE_ERROR);
        }

        return SuccessMessage.CART_ITEM_DELETE_SUCCESS;
    }

    // DTO 변환 메서드
    private ActivityCartResponseDto convertToDto(CartItem cartItem) {
        return ActivityCartResponseDto.of(
                200,
                "장바구니 항목 조회 성공",
                cartItem.getCartItemId(),
                cartItem.getActivity().getActivityId(),
                cartItem.getCart().getGeneralMember().getGeneralMemberId(),
                cartItem.getReserveDate().getReserveDate(),
                cartItem.getReserveTime().getReserveTime(),
                cartItem.getReserveParticipants().getMaxParticipants(),
                cartItem.getActivity().getPrice(),
                cartItem.getCart().getCartId()
        );
    }
}
