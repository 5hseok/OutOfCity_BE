package com.outofcity.server.repository;

import com.outofcity.server.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReserveDateRepository extends JpaRepository<ReserveDate, Long> {
    List<ReserveDate> findByActivity(Activity activity);

}
