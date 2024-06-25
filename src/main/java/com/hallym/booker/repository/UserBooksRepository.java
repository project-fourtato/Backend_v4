package com.hallym.booker.repository;

import com.hallym.booker.domain.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBooksRepository extends JpaRepository<UserBooks, Long> {
}
