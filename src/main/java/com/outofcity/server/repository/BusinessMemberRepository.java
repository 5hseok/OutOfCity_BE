package com.outofcity.server.repository;

import com.outofcity.server.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {
    Optional<BusinessMember> findByBusinessNumber(Long businessNumber);
}
