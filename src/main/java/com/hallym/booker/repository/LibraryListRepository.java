package com.hallym.booker.repository;

import com.hallym.booker.domain.LibraryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibraryListRepository extends JpaRepository<LibraryList, String> {

    // 도서관 이름 검색
    @Query("SELECT ll FROM LibraryList ll WHERE ll.libName LIKE CONCAT('%', :libName, '%')")
    List<LibraryList> findByName(@Param("libName") String libName);
}
