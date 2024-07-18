package com.hallym.booker.repository;

import com.hallym.booker.domain.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookDetailsRepository extends JpaRepository<BookDetails, String> {
    //  isbn에 따른 책 상세정보 조회
    @Query("SELECT bd FROM BookDetails bd WHERE bd.isbn = :isbn")
    BookDetails findByIsbn(@Param("isbn") String isbn);
}
