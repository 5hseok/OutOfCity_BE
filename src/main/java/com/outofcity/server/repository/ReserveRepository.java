package com.outofcity.server.repository;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.Reserve;
import com.outofcity.server.dto.activity.response.ReserveActivityResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findAllByGeneralMember(GeneralMember generalMember);
}
