package com.hallym.booker.controller;

import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.exception.bookDetails.NoSuchBookDetailsException;
import com.hallym.booker.service.BookDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<BookDetailsResponseDTO> getBookDetailsByISBN(@PathVariable String isbn, HttpServletRequest request) {

        // 세션 확인 코드 추가
        HttpSession session = request.getSession(false);
        if (session == null) { // 세션이 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) { // 세션에 회원 데이터가 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        // 기존 로직
        BookDetailsResponseDTO bookDetails = bookDetailsService.getBookDetailsByISBN(isbn);
        if (bookDetails == null) {
            throw new NoSuchBookDetailsException();
        }
        return ResponseEntity.ok(bookDetails);
    }

}