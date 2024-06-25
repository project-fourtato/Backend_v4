package com.hallym.booker.repository;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface JournalsRepository extends JpaRepository<Journals, Long> {
    List<Journals> findByUserBooks_BookUid(Long bookUid);

    //팔로잉 하는 유저들의 최신 독서록 목록 조회에 사용
    List<Journals> findByUserBooks_BookUidOrderByJdatetimeDesc(Long bookUid);

}
