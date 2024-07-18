package com.hallym.booker.service;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.userbooks.ReadingAllBooksListResponse;
import com.hallym.booker.dto.userbooks.ReadingWithAllProfileList;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.BookDetailsRepository;
import com.hallym.booker.repository.ProfileRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 책을 같이 읽는 유저 목록
     */
    public ReadingWithAllProfileList readingWithProfileList(Long profileId) {
        List<UserBooks> withProfileList = userBooksRepository.findWithProfileList(profileId);

        Map<BookDetails, List<Profile>> map = new ConcurrentHashMap<>();
        for (UserBooks userBooks : withProfileList) {
            if(map.containsKey(userBooks.getBookDetails())) {
                List<Profile> profileList = map.get(userBooks.getBookDetails());
                profileList.add(userBooks.getProfile());

                map.put(userBooks.getBookDetails(), profileList);
            } else {
                List<Profile> profileList = new ArrayList<>();
                profileList.add(userBooks.getProfile());

                map.put(userBooks.getBookDetails(), profileList);
            }
        }

        return ReadingWithAllProfileList.from(map);
    }
}
