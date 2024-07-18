package com.hallym.booker.controller;

import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;
import com.hallym.booker.exception.bookDetails.NoSuchBookDetailsException;
import com.hallym.booker.service.BookDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BookDetails")
public class BookDetailsController {

    private final BookDetailsService bookDetailsService;

    // 책 상세정보 조회 API
    @GetMapping("/bookDetailsByISBN/{isbn}")
    public ResponseEntity<BookDetailsResponseDTO> getBookDetailsByISBN(@PathVariable String isbn) {
        BookDetailsResponseDTO bookDetails = bookDetailsService.getBookDetailsByISBN(isbn);
        if (bookDetails == null) {
            throw new NoSuchBookDetailsException();
        }
        return ResponseEntity.ok(bookDetails);
    }
}
