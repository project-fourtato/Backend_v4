package com.hallym.booker.repository;

import com.hallym.booker.domain.Followers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
    Optional<Followers> findByProfile_ProfileUidAndAndFollowerUid(Long profileUid, Long followerUid);

    List<Followers> findByProfile_ProfileUid(Long profileUid);

    Long countByProfile_ProfileUid(Long profileUid);
}
