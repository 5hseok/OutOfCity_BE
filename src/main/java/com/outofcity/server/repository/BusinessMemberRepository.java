package com.outofcity.server.repository;

import com.outofcity.server.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {
    BusinessMember findByBusinessNumber(Long businessNumber);
}
