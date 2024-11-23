package com.outofcity.server.repository;

import com.outofcity.server.domain.Cart;
import com.outofcity.server.domain.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByGeneralMember(GeneralMember generalMember);
}
