package com.hallym.booker.repository;

import com.hallym.booker.domain.Follow;
import com.hallym.booker.dto.Follow.LatestJournalsResponse;
import org.springframework.data.domain.Pageable;
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

    //fromUserId == profile_uid == 본인
    @Query("""
    SELECT new com.hallym.booker.dto.Follow.LatestJournalsResponse(
        p.login.loginUid,
        j.journalId,
        j.jdatetime,
        j.jtitle,
        j.jcontents,
        p.nickname,
        p.userimageUrl,
        p.userimageName
    )
    FROM Journals j
    JOIN j.userBooks ub
    JOIN ub.profile p
    WHERE p.profileUid IN (
        SELECT f.toUserId FROM Follow f WHERE f.profile.profileUid = :fromUserId
    )
    ORDER BY j.jdatetime DESC
""")
    List<LatestJournalsResponse> findLatestJournals(@Param("fromUserId") Long fromUserId, Pageable pageable);

    @Query(value = "SELECT f FROM Follow f WHERE f.toUserId = :to_user_id")
    List<Follow> findAllByToUserId(@Param("to_user_id") Long toUserId);

//    @Query(value = "SELECT COUNT(f) FROM Follow f WHERE f.profile.profileUid = :from_user_id")
//    Long countByFromUserId(@Param("from_user_id") Long fromUserId);
//
//    @Query(value = "SELECT count(f) FROM Follow f WHERE f.toUserId = :to_user_id")
//    Long countByToUserId(@Param("to_user_id") Long toUserId);

    void deleteByToUserIdAndProfile_ProfileUid(Long toUserId, Long profileUid);

}
