package com.outofcity.server.repository;

import com.outofcity.server.domain.ReserveParticipants;
import com.outofcity.server.domain.ReserveTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ReserveParticipantsRepository extends JpaRepository<ReserveParticipants, Long> {

    Optional<ReserveParticipants> findByReserveTime(ReserveTime reserveTime);
}
