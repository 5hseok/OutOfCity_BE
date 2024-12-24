package com.outofcity.server.repository;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findAllByGeneralMember(GeneralMember generalMember);

    List<Reserve> findAllByActivity(Activity activity);

    @Query("SELECT r FROM Reserve r WHERE r.reserveState != 'completed'")
    List<Reserve> findAllNotCompleted();

    @Modifying
    @Query("UPDATE Reserve r SET r.reserveState = 'completed'")
    void updateReserveStateToCompleted(Reserve reserve);
}
