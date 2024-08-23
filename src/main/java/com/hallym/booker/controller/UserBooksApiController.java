package com.hallym.booker.controller;

import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.LibraryList.LibraryListDTO;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.Result;
import com.hallym.booker.dto.UserBooks.*;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.UserBooksRepository;
import com.hallym.booker.service.LibraryListService;
import com.hallym.booker.service.UserBooksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserBooksApiController {
    private final UserBooksService userBooksService;
    private final JournalsRepository journalsRepository;
    private final UserBooksRepository userBooksRepository;
    private final LibraryListService libraryListService;

    @GetMapping("/booksList")
    public ResponseEntity<ReadingAllBooksListResponse> readingAllBooksList(HttpServletRequest request, @RequestParam(value = "userId", required = false) String userId) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        if(userId == null) {
            return ResponseEntity.ok().body(userBooksService.readingAllBooksList(loginResponse.getUid()));
        } else {
            return ResponseEntity.ok().body(userBooksService.readingAllBooksList(userId));
        }
    }

    @GetMapping("/books")
    public ResponseEntity<ReadingWithAllUserList> readingWithAllProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        return ResponseEntity.ok().body(userBooksService.readingWithProfileList(loginResponse.getUid()));
    }

    @GetMapping("/bestseller")
    public BookFindAllDTO bestseller() {
        return userBooksService.bestseller();
    }

    // 책 등록 API
    @PostMapping("/search/books/new/")
    public ResponseEntity<String> saveUserBooks(HttpServletRequest request, @RequestBody UserBooksDTO userBooksDTO) {

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
        userBooksDTO.setProfileUid(loginResponse.getUid());

        userBooksService.saveUserBooks(loginResponse.getUid(), userBooksDTO);
        return ResponseEntity.ok().body("책 등록 성공");
    }

    // 책 삭제 API
    @PostMapping("/books/{bookUid}/delete")
    public ResponseEntity<BookDeleteResponseDTO> booksDelete(@PathVariable("bookUid") Long bookUid, HttpServletRequest request) {

        // 세션 확인 코드 추가
        HttpSession session = request.getSession(false);
        if (session == null) { // 세션이 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) { // 세션에 회원 데이터가 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        return ResponseEntity.ok().body(new BookDeleteResponseDTO(userBooksService.deleteUserBooks(bookUid)));
    }

    // 독서 상태 변경 API
    @PutMapping("/readStatusUpdate/{bookUid}")
    public ResponseEntity<?> updateReadStatus(@PathVariable Long bookUid, @RequestBody UserBooksUpdateRequestDTO updateDTO, HttpServletRequest request) {

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
        String responseMessage = userBooksService.updateReadStatus(bookUid, updateDTO);
        return ResponseEntity.ok().body(Map.of("data", responseMessage));
    }

    // 책 판매 상태 변경 API
    @PutMapping("/saleStatusUpdate/{bookUid}")
    public ResponseEntity<?> updateSaleStatus(@PathVariable Long bookUid, @RequestBody UserBooksUpdateRequestDTO updateDTO, HttpServletRequest request) {

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
        String responseMessage = userBooksService.updateSaleStatus(bookUid, updateDTO);
        return ResponseEntity.ok().body(Map.of("data", responseMessage));
    }

    // 한 책에 대한 독서 상태 조회 API
    @GetMapping("/readStatus")
    public ResponseEntity<UserBooksReadStatusResponseDTO> getReadStatus(HttpServletRequest request, @RequestParam("isbn") String isbn) {

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
        UserBooksReadStatusResponseDTO responseDTO = userBooksService.getReadStatus(loginResponse.getUid(), isbn);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 책 검색에서 isbn을 통해 독서 상태 및 책(알라딘) 조회 API
    @GetMapping("/searchByISBN")
    public ResponseEntity<List<BooksWithStatusDTO>> searchBooks(@PathVariable("searchOne") String searchOne, HttpServletRequest request) {

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
        List<BooksWithStatusDTO> booksList = userBooksService.searchBooks(loginResponse.getUid(), searchOne);
        return ResponseEntity.ok().body(booksList);
    }

    // 책 교환에서 검색된 책 목록 조회 API
    @GetMapping("/sale/searchOne/{searchOne}")
    public ResponseEntity<BookFindAllDTO> booksFind(@PathVariable("searchOne") String searchOne, HttpServletRequest request) {

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
        return ResponseEntity.ok().body(userBooksService.searchBooks(searchOne));
    }

    // 책 교환에서 isbn과 salesstate을 이용하여 profileUid 추출 후 프로필 목록 조회 API
    @GetMapping("/sale/isbn/saleStatus/profileList")
    public ResponseEntity<List<ProfileResponseDTO>> getProfilesByIsbnAndSaleStatus(@RequestParam String isbn, HttpServletRequest request) {

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
        List<ProfileResponseDTO> responseDTOs = userBooksService.getProfilesByIsbnAndSaleStatus(isbn);
        return ResponseEntity.ok().body(responseDTOs);
    }

    // 도서관 검색에서 region과 dtl_region, isbn을 이용하여 해당 책을 소장하고 있는 지역 도서관 목록 조회 API
    @GetMapping("/libraryList/region={region}&dtl_region={dtl_region}&isbn={isbn}")
    public ResponseEntity<Result<LibraryListDTO>> librarySearchWithBook(@PathVariable("region") String region, @PathVariable("dtl_region") String dtl_region, @PathVariable("isbn") String isbn, HttpServletRequest request) {

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
        List<LibraryListDTO> libraryList = libraryListService.findLibrariesByRegionAndISBN(region, dtl_region, isbn);
        return ResponseEntity.ok().body(new Result<>(libraryList));
    }

    // 도서관 검색에서 region과 dtl_region, searchOne 이용하여 해당 지역 도서관 조회 API
    @GetMapping("/libraryList/region/{region}/dtl_region/{dtl_region}/searchOne/{searchOne}")
    public ResponseEntity<Result<LibraryListDTO>> librarySearch(@PathVariable("region") String region, @PathVariable("dtl_region") String dtl_region, @PathVariable("searchOne") String searchOne, HttpServletRequest request) {

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
        List<LibraryListDTO> libraries = (List<LibraryListDTO>) libraryListService.librarySearch(region, dtl_region, searchOne);
        return ResponseEntity.ok().body(new Result<>(libraries));
    }
}

