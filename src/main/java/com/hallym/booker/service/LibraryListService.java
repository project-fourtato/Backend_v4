package com.hallym.booker.service;

import com.hallym.booker.dto.LibraryList.LibraryListDTO;

import java.util.List;

public interface LibraryListService {

    // 지역, 세부 지역, ISBN을 이용하여 해당 책을 소장하는 도서관 목록 조회
    List<LibraryListDTO> findLibrariesByRegionAndISBN(String region, String dtl_region, String isbn);

    // 도서관 이름으로 검색하여 도서관 코드 리스트 반환
    List<String> searchLibrary(String name);

    // 주어진 URL을 통해 도서관 목록 조회
    List<LibraryListDTO> searchLibraries(String url);

    // 지역, 세부 지역, 검색어를 이용하여 도서관 목록 조회
    List<LibraryListDTO> librarySearch(String region, String dtl_region, String searchOne);
}