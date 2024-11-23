package com.outofcity.server.repository;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByNameAndBusinessMember(String name, BusinessMember businessMember);

    List<Activity> findAllByMainCategory(String mainCategory);
}
