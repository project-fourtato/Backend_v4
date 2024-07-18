package com.hallym.booker.service;

import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.ReadingAllBooksListResponse;
import com.hallym.booker.dto.ReadingBookResponse;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.BookDetailsRepository;
import com.hallym.booker.repository.ProfileRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBooksService {
    private final UserBooksRepository userBooksRepository;
    private final BookDetailsRepository bookDetailsRepository;
    private final ProfileRepository profileRepository;

    /**
     * 읽고 있는 책 목록
     */
    public ReadingAllBooksListResponse readingAllBooksList(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(NoSuchProfileException::new);

        List<UserBooks> allByProfile = userBooksRepository.findAllByProfile(profile);
        return ReadingAllBooksListResponse.from(allByProfile);
    }
}
