package com.outofcity.server.repository;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {

    default GeneralMember findByToken(String token, JwtTokenProvider jwtTokenProvider) {
        return findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER)); // userId로 User 객체 조회
    }

}
