package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Journals.JournalSaveRequest;
import com.hallym.booker.dto.Journals.JournalsEditFormResponse;
import com.hallym.booker.dto.Journals.JournalsEditRequest;
import com.hallym.booker.repository.*;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class JournalsServiceTest {
    @Autowired
    JournalsService journalsService;
    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserBooksRepository userBooksRepository;
    @Autowired
    BookDetailsRepository bookDetailsRepository;


    Long profile1Id = 0L;
    Long profile2Id = 0L;
    Long userBooksId = 0L;

    @Before
    public void setUp() {
        //Given
        Login login1 = Login.create("gamja","gamjapw","gamja@gmail.com",new Date(2000, 12, 21));
        login1 = loginRepository.save(login1);
        Login login2 = Login.create("dubu","dubupw","dubu@gmail.com",new Date(2000, 07, 03));
        login2 = loginRepository.save(login2);

        Profile profile1 = Profile.create(login1,"감자","https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png","/default/default-profile.png","무력 감자");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(login2,"두부","https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png","/default/default-profile.png","무기력 두부");
        profile2 = profileRepository.save(profile2);

        profile1Id = profile1.getProfileUid();
        profile2Id = profile2.getProfileUid();

        BookDetails bookDetails = BookDetails.create("1452-84566", "해리포터", "JK롤링", "출판사", "https://book-image");
        bookDetails = bookDetailsRepository.save(bookDetails);

        UserBooks userBooks = UserBooks.create(profile1, bookDetails, 0, 0);
        userBooks = userBooksRepository.save(userBooks);
        userBooksId = userBooks.getBookUid();
    }

    @Test
    public void  journalsSaveTest() {
        //given
        JournalSaveRequest journalSaveRequest = new JournalSaveRequest(userBooksId, "해리포터 잼따..", "해리포터 진짜 잼쓰니까 다 봐봐여,,",
                "https://default-image", "default-image");

        //when
        journalsService.journalSave(journalSaveRequest);

        //then
        List<Journals> journalsList = journalsRepository.findAll();
        Assertions.assertThat(journalsList.size()).isEqualTo(1);
    }

    @Test
    public void getJournalsEditFormTest() {
        //given
        JournalSaveRequest journalSaveRequest = new JournalSaveRequest(userBooksId, "해리포터 잼따..", "해리포터 진짜 잼쓰니까 다 봐봐여,,",
                "https://default-image", "default-image");
        journalsService.journalSave(journalSaveRequest);
        List<Journals> journalsList = journalsRepository.findAll();

        //when
        JournalsEditFormResponse journalsEditForm = journalsService.getJournalsEditForm(journalsList.get(0).getJournalId());

        //then
        Assertions.assertThat(journalsEditForm.getJtitle()).isEqualTo("해리포터 잼따..");
    }

    @Test
    public void journalsEdit() throws IOException {
        //given
        JournalSaveRequest journalSaveRequest = new JournalSaveRequest(userBooksId, "해리포터 잼따..", "해리포터 진짜 잼쓰니까 다 봐봐여,,",
                "https://default-image", "default-image");
        journalsService.journalSave(journalSaveRequest);
        List<Journals> journalsList = journalsRepository.findAll();

        Journals journals = journalsList.get(0);
        JournalsEditRequest journalsEditRequest = new JournalsEditRequest(journalsList.get(0).getJournalId(), "해리포터는 아즈카반의 죄수가 젤 잼씀,,", journals.getJcontents(), null);

        //when
        journalsService.journalsEdit(journalsEditRequest);

        //then
        Assertions.assertThat(journals.getJtitle()).isEqualTo("해리포터는 아즈카반의 죄수가 젤 잼씀,,");
    }

}