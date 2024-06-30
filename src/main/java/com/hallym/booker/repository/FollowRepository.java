package com.hallym.booker.repository;

import com.hallym.booker.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "SELECT f FROM Follow f WHERE f.profile.profileUid = :from_user_id AND f.toUserId = :to_user_id")
    Optional<Follow> findByFromUserIdAndToUserId(@Param("from_user_id") Long from_user_id, @Param("to_user_id") Long to_user_id);

    @Query(value = "SELECT f FROM Follow f WHERE f.profile.profileUid = :from_user_id")
    List<Follow> findAllByFromUserId(@Param("from_user_id") Long fromUserId);

    @Query(value = "SELECT f FROM Follow f WHERE f.toUserId = :to_user_id")
    List<Follow> findAllByToUserId(@Param("to_user_id") Long toUserId);

    @Query(value = "SELECT COUNT(f) FROM Follow f WHERE f.profile.profileUid = :from_user_id")
    Long countByFromUserId(@Param("from_user_id") Long fromUserId);

    @Query(value = "SELECT count(f) FROM Follow f WHERE f.toUserId = :to_user_id")
    Long countByToUserId(@Param("to_user_id") Long toUserId);
}
