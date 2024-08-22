package com.hallym.booker.repository;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserBooksRepository extends JpaRepository<UserBooks, Long> {

    //특정 프로필의 모든 책 조회
    @Query("SELECT ub FROM UserBooks ub WHERE ub.profile = :profile")
    List<UserBooks> findAllByProfile(@Param("profile") Profile profile);

    // 특정 프로필의 모든 책 읽기 상태 조회
    @Query("SELECT ub.readStatus FROM UserBooks ub WHERE ub.profile = :profile")
    List<Integer> findReadStatusByProfile(@Param("profile") Profile profile);

    //특정 프로필의 모든 책 판매 상태 조회
    @Query("SELECT ub.saleStatus FROM UserBooks ub WHERE ub.profile = :profile")
    List<Integer> findSaleStatusByProfile(@Param("profile") Profile profile);

    // isbn으로 해당 책을 가진 모든 프로필 조회
    @Query("SELECT ub.profile FROM UserBooks ub WHERE ub.bookDetails.isbn = :isbn")
    List<Profile> findAllProfilesByIsbn(@Param("isbn") String isbn);

    // 책 검색에서 ub.profile과 ISBN으로 책 읽기 상태 조회
    @Query("SELECT ub FROM UserBooks ub WHERE ub.bookDetails.isbn = :isbn AND ub.profile = :profile")
    UserBooks findByProfileUidAndIsbn(@Param("profile") Profile profile, @Param("isbn") String isbn);

    // 주어진 ISBN과 판매 상태가 활성화된 책을 가진 프로필들을 조회
    @Query("SELECT ub.profile FROM UserBooks ub WHERE ub.bookDetails.isbn = :isbn AND ub.saleStatus = 1")
    List<Profile> findByIsbnAndSalesstate(@Param("isbn") String isbn);

    // 책 등록
    @Query("SELECT ub FROM UserBooks ub WHERE ub.profile = :profile AND ub.bookDetails = :bookDetails")
    List<UserBooks> findByProfileAndBookDetails(@Param("profile") Profile profile, @Param("bookDetails") BookDetails bookDetails);


    // 같이 읽고 있는 유저 목록
    @Query("SELECT ub2 " +
            "FROM UserBooks ub1 JOIN FETCH UserBooks ub2 ON ub1.bookDetails.isbn = ub2.bookDetails.isbn " +
            "JOIN FETCH Follow f ON f.toUserId = :profileId " +
            "WHERE ub1.profile.profileUid = :profileId")
    List<UserBooks> findWithProfileList(@Param("profileId") Long profileId);

    //isbn과 유저Id로 같이 읽고 있는 사람 조회
}