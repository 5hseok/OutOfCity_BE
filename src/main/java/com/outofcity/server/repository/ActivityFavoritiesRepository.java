package com.outofcity.server.repository;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.ActivityFavorities;
import com.outofcity.server.domain.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityFavoritiesRepository extends JpaRepository<ActivityFavorities, Long> {

    List<ActivityFavorities> findAllByGeneralMember(GeneralMember generalMember);

    Optional<ActivityFavorities> findByActivityAndGeneralMember(Activity activity, GeneralMember generalMember);
}
