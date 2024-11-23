package com.outofcity.server.repository;

import com.outofcity.server.domain.Activity;
import com.outofcity.server.domain.ActivityImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityImageRepository extends JpaRepository<ActivityImage, Long> {
    List<ActivityImage> findAllByActivity(Activity activity);
}
