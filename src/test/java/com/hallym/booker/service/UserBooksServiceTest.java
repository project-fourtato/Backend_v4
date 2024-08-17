package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.UserBooks.ReadingAllBooksListResponse;
import com.hallym.booker.dto.UserBooks.ReadingProfile;
import com.hallym.booker.dto.UserBooks.ReadingWithAllProfileList;
import com.hallym.booker.dto.UserBooks.ReadingWithProfile;
import com.hallym.booker.exception.login.NoSuchProfileException;
import com.hallym.booker.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class UserBooksServiceTest {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserBooksRepository userBooksRepository;
    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    BookDetailsRepository bookDetailsRepository;
    @Autowired
    UserBooksService userBooksService;

    Long profile1Id = 0L;
    Long profile2Id = 0L;

    @Before
    public void setUp() {
        Date now = new Date();
        Login login1 = Login.create("id1","pw1","email1",now);
        login1 = loginRepository.save(login1);
        Login login2 = Login.create("id2","pw2","email2",now);
        login2 = loginRepository.save(login2);

        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png","/default/default-profile.png","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png","/default/default-profile.png","책 안좋아해요");
        profile2 = profileRepository.save(profile2);

        profile1Id = profile1.getProfileUid();
        profile2Id = profile2.getProfileUid();

        BookDetails bookDetails1 = BookDetails.create("isbn12","어린왕자","생텍쥐페리","다연","resources/DummyImg/s3UploadedImg.png");
        BookDetails bookDetails2 = BookDetails.create("isbn34","어린왕자","생텍쥐페리","민음사","resources/DummyImg/s3UploadedImg.png");
        bookDetails1 = bookDetailsRepository.save(bookDetails1);
        bookDetails2 = bookDetailsRepository.save(bookDetails2);

        UserBooks userBooks1 = UserBooks.create(profile1, bookDetails1,0,0);
        UserBooks userBooks2 = UserBooks.create(profile1, bookDetails2,1,0);
        userBooks1 = userBooksRepository.save(userBooks1);
        userBooks2 = userBooksRepository.save(userBooks2);

        Journals journals1 = Journals.create(userBooks1, "책 너무 감동적","인생작", LocalDateTime.now(),"resources/DummyImg/S3BasicImg.jpg","default");
        Journals journals2 = Journals.create(userBooks2, "다른 출판사껀데 이것도 너무 감동적","인생작",LocalDateTime.now(),"resources/DummyImg/S3BasicImg.jpg","default");
        journals1 = journalsRepository.save(journals1);
        journals2 = journalsRepository.save(journals2);
    }
    @Test
    public void readingAllBooksListTest() {
        //given
        Login login = profileRepository.findById(profile1Id).orElseThrow(NoSuchProfileException::new).getLogin();

        //when
        ReadingAllBooksListResponse readingAllBooksListResponse = userBooksService.readingAllBooksList(login.getLoginUid());

        //then
        Assertions.assertThat(readingAllBooksListResponse.getResult().size()).isEqualTo(2);
    }

    @Test
    public void readingWithProfileListTest() {
        //given
        String loginId = profileRepository.findById(profile1Id).orElseThrow(NoSuchProfileException::new).getLogin().getLoginUid();

        Date now = new Date();
        Login login3 = Login.create("id3","pw3","email3",now);
        login3 = loginRepository.save(login3);
        Profile profile3 = Profile.create(login3,"감자","https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png","/default/default-profile.png","책 안좋아해요");
        profile3 = profileRepository.save(profile3);

        BookDetails bookDetails3 = BookDetails.create("isbn100","해리포터","JK롤링","민음사","resources/DummyImg/s3UploadedImg.png");
        bookDetails3 = bookDetailsRepository.save(bookDetails3);

        UserBooks userBooks2 = UserBooks.create(profile3, bookDetails3,1,0);
        userBooks2 = userBooksRepository.save(userBooks2);
        UserBooks userBooks3 = UserBooks.create(profileRepository.findById(profile1Id).get(), bookDetails3,1,0);
        userBooks3 = userBooksRepository.save(userBooks3);

        //when
        ReadingWithAllProfileList readingWithAllProfileList = userBooksService.readingWithProfileList(loginId);
        List<ReadingWithProfile> readingWithProfiles = readingWithAllProfileList.getResult();
        ReadingWithProfile readingWithProfile = readingWithProfiles.get(0);
        List<ReadingProfile> profiles = readingWithProfile.getProfileList();

        //then
        Assertions.assertThat(profiles.get(0).getNickname()).isEqualTo("감자");

    }
}