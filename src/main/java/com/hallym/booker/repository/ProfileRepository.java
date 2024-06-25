package com.hallym.booker.repository;

import com.hallym.booker.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
