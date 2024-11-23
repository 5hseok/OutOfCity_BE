package com.outofcity.server.repository;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.ReserveDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDateRepository extends JpaRepository<ReserveDate, Long> {
    List<ReserveDate> findByActivity(Activity activity);

}
