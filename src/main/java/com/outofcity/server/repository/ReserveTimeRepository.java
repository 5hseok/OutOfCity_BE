package com.outofcity.server.repository;

import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.ReserveDate;
import com.outofcity.server.domain.ReserveTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReserveTimeRepository extends JpaRepository<ReserveTime, Long> {

    List<ReserveTime> findByReserveDate(ReserveDate reserveDate);

}
