package com.outofcity.server.repository;

import com.outofcity.server.domain.Challenge;
import com.outofcity.server.domain.GeneralMember;
import com.outofcity.server.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    List<UserChallenge> findByGeneralMember(GeneralMember generalMember);

    Optional<UserChallenge> findByGeneralMemberAndPerformedAt(GeneralMember generalMember, LocalDate performedAt);
}
