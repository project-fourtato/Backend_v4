package com.hallym.booker.repository;

import com.hallym.booker.domain.*;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class JournalsRepositoryTest {
    @Autowired
    private JournalsRepository journalsRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserBooksRepository userBooksRepository;
    @Autowired
    private BookDetailsRepository bookDetailsRepository;
    @Test
    void test_findByUserBooks_BookUid() {
        //Given
        Date now = new Date();

        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        Profile profile = Profile.create(login_0,"닉네임","imageurl","imagename", "안녕하세요");
        Profile profile_0 = profileRepository.save(profile);
        BookDetails bookDetails = BookDetails.create("isbn","어린왕자","dy","dy","url");
        BookDetails bookDetails1_0 = bookDetailsRepository.save(bookDetails);
        UserBooks userBooks = UserBooks.create(profile_0,bookDetails1_0,0,0);
        UserBooks userBooks_0 = userBooksRepository.save(userBooks);
        Journals journals = Journals.create(userBooks_0,"어린왕자를 읽고..","쌸라쌸라",LocalDateTime.now(),"url","name");
        journalsRepository.save(journals);
        //When
        List<Journals> journalsList = journalsRepository.findByUserBooks_BookUid(userBooks_0.getBookUid());
        //Then
        org.assertj.core.api.Assertions.assertThat(journalsList.size()).isEqualTo(1);
    }

    @Test
    void findByUserBooks_BookUidOrderByJdatetimeDesc() {
        //Given
        Date now = new Date();

        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        Profile profile = Profile.create(login_0,"닉네임","imageurl","imagename", "안녕하세요");
        Profile profile_0 = profileRepository.save(profile);
        BookDetails bookDetails = BookDetails.create("isbn","어린왕자","dy","dy","url");
        BookDetails bookDetails1_0 = bookDetailsRepository.save(bookDetails);
        UserBooks userBooks = UserBooks.create(profile_0,bookDetails1_0,0,0);
        UserBooks userBooks_0 = userBooksRepository.save(userBooks);
        Journals journals = Journals.create(userBooks_0,"어린왕자를 읽고..","쌸라쌸라",LocalDateTime.now(),"url","name");
        journalsRepository.save(journals);
        Journals journals2 = Journals.create(userBooks_0,"백설공주를 읽고..","쌸라쌸라",LocalDateTime.of(2025,1,1,1,1),"url","name");
        journalsRepository.save(journals2);
        //When
        List<Journals> journalsList = journalsRepository.findByUserBooks_Profile_ProfileUidOrderByJdatetimeDesc(userBooks_0.getProfile().getProfileUid());
        //Then
        org.assertj.core.api.Assertions.assertThat(journalsList.size()).isEqualTo(2);
        Assertions.assertThat(journalsList.get(0).getJtitle()).isEqualTo("백설공주를 읽고..");
    }
}