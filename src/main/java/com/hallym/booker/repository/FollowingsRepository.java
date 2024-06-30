package com.hallym.booker.repository;

import com.hallym.booker.domain.Followings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowingsRepository extends JpaRepository<Followings, Long> {

    Optional<Followings> findByProfile_ProfileUidAndAndFollowingUid(Long profileUid, Long followingUid);

    List<Followings> findByProfile_ProfileUid(Long profileUid);

    Long countByProfile_ProfileUid(Long profileUid);

}
