package com.hallym.booker.repository;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Profile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface InterestsRepository extends JpaRepository<Interests, Long> {

    //특정 프로필에 대한 모든 관심사 조회
    List<Interests> findByProfile_ProfileUid(Long profileUid);

    //모든 관심사들 조회(본인 제외)
    @Query("SELECT i FROM Interests i where i.profile.profileUid not in :profileUid")
    List<Interests> findByProfile_ProfileUidNotIn(@Param("profileUid") Long profileUid);

    //해당 유저의 관심사 전체 삭제
    @Modifying
    @Query("DELETE FROM Interests i WHERE i.profile.profileUid = :profileUid")
    void deleteAllByProfile_ProfileUid(@Param("profileUid") Long profileUid);

    @Query("SELECT i2.profile " +
            "FROM Interests i1 " +
            "JOIN Interests i2 ON i1.interestName = i2.interestName " +
            "WHERE i1.profile.profileUid = :profileUid " +
            "GROUP BY i2.profile.profileUid " +
            "HAVING COUNT(i2.interestName) >= 2")
    List<Profile> findSameInterestProfile(@Param("profileUid") Long profileUid);
}
