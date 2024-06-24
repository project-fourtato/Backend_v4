package com.hallym.booker.repository;

import com.hallym.booker.domain.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDetailsRepository extends JpaRepository<BookDetails, String> {
}
