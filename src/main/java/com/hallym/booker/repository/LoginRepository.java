package com.hallym.booker.repository;

import com.hallym.booker.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface LoginRepository extends JpaRepository<Login, String> {

    //회원 로그인
    Optional<Login> findByLoginUidAndPw(String loginUid, String pw);

    // foreign key constraint fails 에러를 위한 생쿼리문
//    @Modifying
//    @Query(value = "SET foreign_key_checks = 0", nativeQuery = true)
//    void disableForeignKeyChecks();
//
//    @Modifying
//    @Query(value = "DELETE FROM :login", nativeQuery = true)
//    void deleteTableWithForeignKeyChecks(Login login);
//
//    @Modifying
//    @Query(value = "SET foreign_key_checks = 1", nativeQuery = true)
//    void enableForeignKeyChecks();
}
