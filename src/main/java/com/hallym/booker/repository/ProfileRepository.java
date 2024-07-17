package com.hallym.booker.repository;

import com.hallym.booker.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p FROM Profile p WHERE p.nickname like %:nickname%")
    List<Profile> findAllByNickname(@Param("nickname") String nickname);
}
