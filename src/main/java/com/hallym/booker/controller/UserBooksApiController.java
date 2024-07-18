package com.hallym.booker.controller;

import com.hallym.booker.dto.userbooks.BestSellerListResponse;
import com.hallym.booker.dto.userbooks.ReadingAllBooksListResponse;
import com.hallym.booker.dto.userbooks.ReadingWithAllProfileList;
import com.hallym.booker.service.UserBooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserBooksApiController {
    private final UserBooksService userBooksService;

    @GetMapping("/booksList/{profileId}")
    public ReadingAllBooksListResponse readingAllBooksList(@PathVariable Long profileId) {
        return userBooksService.readingAllBooksList(profileId);
    }

    @GetMapping("/books/{profileId}")
    public ReadingWithAllProfileList readingWithAllProfile(@PathVariable Long profileId) {
        return userBooksService.readingWithProfileList(profileId);
    }

    @GetMapping("/bestseller")
    public BestSellerListResponse bestseller() {
        return userBooksService.bestseller();
    }
}
