
package com.hallym.booker.service;

import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.UserBooks.*;
import org.w3c.dom.Element;

import java.util.List;

public interface UserBooksService {

    // 책 등록
    void saveUserBooks(String loginUid, UserBooksDTO userBooksDTO);

    // 독서 상태 변경
    String updateReadStatus(Long bookUid, UserBooksUpdateRequestDTO updateDTO);

    // 책 판매 상태 변경
    String updateSaleStatus(Long bookUid, UserBooksUpdateRequestDTO updateDTO);

    // 한 책에 대한 독서 상태 조회
    UserBooksReadStatusResponseDTO getReadStatus(String loginUid, String isbn);

    // 책 검색에서 isbn을 통해 독서 상태 및 책(알라딘) 조회
    List<BooksWithStatusDTO> searchBooks(String loginUid, String searchOne);

    // 책 교환에서 검색된 책 목록 조회
    List<BooksFindDTO> searchBooks(String searchOne);

    // 책 교환에서 isbn과 salesstate을 이용하여 profileUid 추출 후 프로필 목록 조회
    List<ProfileResponseDTO> getProfilesByIsbnAndSaleStatus(String isbn);

    //읽고 있는 책 목록
    ReadingAllBooksListResponse readingAllBooksList(String loginId);

    //책을 같이 읽는 유저 목록
    ReadingWithAllProfileList readingWithProfileList(String loginId);

    //베스트셀러
    BestSellerListResponse bestseller();

    // tag값의 정보를 가져오는 함수
    String getTagValue(String tag, Element eElement);
}