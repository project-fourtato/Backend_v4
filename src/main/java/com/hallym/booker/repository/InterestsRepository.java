package com.hallym.booker.repository;

import com.hallym.booker.domain.Interests;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
