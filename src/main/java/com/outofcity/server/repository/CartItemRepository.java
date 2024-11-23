package com.outofcity.server.repository;

import com.outofcity.server.domain.Cart;
import com.outofcity.server.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}