package com.outofcity.server.repository;

import com.outofcity.server.domain.ReserveDate;
import com.outofcity.server.domain.ReserveTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveTimeRepository extends JpaRepository<ReserveTime, Long> {

    List<ReserveTime> findAllByReserveDate(ReserveDate reserveDate);

}
