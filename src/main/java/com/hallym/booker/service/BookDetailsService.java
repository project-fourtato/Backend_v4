package com.hallym.booker.service;

import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;

public interface BookDetailsService {

    // 책 상세정보 조회
    BookDetailsResponseDTO getBookDetailsByISBN(String isbn);
}

