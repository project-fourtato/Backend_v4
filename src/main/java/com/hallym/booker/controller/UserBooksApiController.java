package com.hallym.booker.controller;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.UserBooks.*;
import com.hallym.booker.dto.userbooks.BestSellerListResponse;
import com.hallym.booker.dto.userbooks.ReadingAllBooksListResponse;
import com.hallym.booker.dto.userbooks.ReadingWithAllProfileList;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.UserBooksRepository;
import com.hallym.booker.service.UserBooksService;
import com.hallym.booker.service.UserBooksServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserBooksApiController {
    private final UserBooksService userBooksService;
    private final UserBooksServiceImpl userBooksServiceImpl;
    private final JournalsRepository journalsRepository;
    private final UserBooksRepository userBooksRepository;


    @GetMapping("/booksList/{profileId}")
    public ReadingAllBooksListResponse readingAllBooksList(@PathVariable Long profileId) {
        return userBooksServiceImpl.readingAllBooksList(profileId);
    }

    @GetMapping("/books/{profileId}")
    public ReadingWithAllProfileList readingWithAllProfile(@PathVariable Long profileId) {
        return userBooksServiceImpl.readingWithProfileList(profileId);
    }

    @GetMapping("/bestseller")
    public BestSellerListResponse bestseller() {
        return userBooksServiceImpl.bestseller();
    }

    // 책 등록 API
    @PostMapping("/search/books/new/{profileUid}")
    public ResponseEntity<String> saveUserBooks(@PathVariable Long profileUid, @RequestBody UserBooksDTO userBooksDTO) {
        userBooksDTO.setProfileUid(profileUid);

        userBooksService.saveUserBooks(userBooksDTO);
        return ResponseEntity.ok().body("책 등록 성공");
    }

    // 책 삭제 API
    @PostMapping("/{bookUid}/delete")
    public ResponseEntity<BookDeleteResponseDTO> booksDelete(@PathVariable("bookUid") Long bookUid) {
        List<Journals> allJournalsByBookUid = journalsRepository.findByUserBooks_BookUid(bookUid);
        allJournalsByBookUid.forEach(journalsRepository::delete);

        UserBooks userBooks = userBooksRepository.findById(bookUid)
                .orElseThrow(() -> new NoSuchUserBooksException());

        userBooksRepository.delete(userBooks);
        return ResponseEntity.ok().body(new BookDeleteResponseDTO("책 삭제 성공"));
    }

    // 독서 상태 변경 API
    @PutMapping("/readStatusUpdate/{bookUid}")
    public ResponseEntity<?> updateReadStatus(@PathVariable Long bookUid, @RequestBody UserBooksUpdateRequestDTO updateDTO) {
        String responseMessage = userBooksService.updateReadStatus(bookUid, updateDTO);
        return ResponseEntity.ok().body(Map.of("data", responseMessage));
    }

    // 책 판매 상태 변경 API
    @PutMapping("/saleStatusUpdate/{bookUid}")
    public ResponseEntity<?> updateSaleStatus(@PathVariable Long bookUid, @RequestBody UserBooksUpdateRequestDTO updateDTO) {
        String responseMessage = userBooksService.updateSaleStatus(bookUid, updateDTO);
        return ResponseEntity.ok().body(Map.of("data", responseMessage));
    }

    // 한 책에 대한 독서 상태 조회 API
    @GetMapping("/readStatus")
    public ResponseEntity<UserBooksReadStatusResponseDTO> getReadStatus(@RequestParam("profileUid") Long profileUid, @RequestParam("isbn") String isbn) {
        UserBooksReadStatusResponseDTO responseDTO = userBooksService.getReadStatus(profileUid, isbn);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 책 검색에서 isbn을 통해 독서 상태 및 책(알라딘) 조회 API
    @GetMapping("/searchByISBN/{profileUid}")
    public ResponseEntity<List<BooksWithStatusDTO>> searchBooks(@PathVariable Long profileUid, @RequestParam String isbn) {
        List<BooksWithStatusDTO> booksList = userBooksService.searchBooks(profileUid, isbn);
        return ResponseEntity.ok().body(booksList);
    }

    // 책 교환에서 검색된 책 목록 조회 API
    @GetMapping("/sale/searchOne/{searchOne}")
    public ResponseEntity<List<BooksFindDTO>> booksFind(@PathVariable("searchOne") String searchOne) {
        List<BooksFindDTO> booksList = userBooksService.searchBooks(searchOne);
        return ResponseEntity.ok().body(booksList);
    }

    // 책 교환에서 isbn과 salesstate을 이용하여 profileUid 추출 후 프로필 목록 조회 API
    @GetMapping("/sale/isbn/saleStatus/profileList")
    public ResponseEntity<List<ProfileResponseDTO>> getProfilesByIsbnAndSaleStatus(@RequestParam String isbn) {
        List<ProfileResponseDTO> responseDTOs = userBooksService.getProfilesByIsbnAndSaleStatus(isbn);
        return ResponseEntity.ok().body(responseDTOs);
    }

    // 도서관 검색에서 region과 dtl_region, isbn을 이용하여 해당 책을 소장하고 있는 지역 도서관 목록 조회 API
    @GetMapping("/libraryList/region={region}&dtl_region={dtl_region}&isbn={isbn}")
    public ResponseEntity<Result<List<LibraryListDTO>>> librarySearchWithBook(@PathVariable("region") String region, @PathVariable("dtl_region") String dtl_region, @PathVariable("isbn") String isbn) {
        List<LibraryListDTO> libraryList = libraryListService.findLibrariesByRegionAndISBN(region, dtl_region, isbn);
        return ResponseEntity.ok().body(new Result<>(libraryList));
    }

    // 도서관 검색에서 region과 dtl_region, searchOne 이용하여 해당 지역 도서관 조회 API
    @GetMapping("/libraryList/region/{region}/dtl_region/{dtl_region}/searchOne/{searchOne}")
    public ResponseEntity<Result<List<LibraryListDTO>>> librarySearch(@PathVariable("region") String region, @PathVariable("dtl_region") String dtl_region, @PathVariable("searchOne") String searchOne) {
        List<LibraryListDTO> libraries = (List<LibraryListDTO>) libraryListService.librarySearch(region, dtl_region, searchOne);
        return ResponseEntity.ok().body(new Result<>(libraries));
    }
}
