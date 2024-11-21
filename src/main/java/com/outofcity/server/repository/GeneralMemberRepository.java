package com.outofcity.server.repository;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import com.outofcity.server.global.jwt.JwtTokenProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {

}
