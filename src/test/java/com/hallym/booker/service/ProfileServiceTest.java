package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Profile.*;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class ProfileServiceTest {
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    InterestsRepository interestsRepository;
    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    BookDetailsRepository bookDetailsRepository;
    @Autowired
    DirectmessageRepository directmessageRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    UserBooksRepository userBooksRepository;

    Long profile1Id = 0L;
    Long profile2Id = 0L;

    @BeforeEach
    void setUp() {
        //Given
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

        Interests interests1 = Interests.create("호러",profile1);
        Interests interests2 = Interests.create("로맨스",profile1);
        interestsRepository.save(interests1);
        interestsRepository.save(interests2);

        BookDetails bookDetails1 = BookDetails.create("isbn12","어린왕자","생텍쥐페리","다연","resources/DummyImg/s3UploadedImg.png");
        BookDetails bookDetails2 = BookDetails.create("isbn34","어린왕자","생텍쥐페리","민음사","resources/DummyImg/s3UploadedImg.png");
        bookDetails1 = bookDetailsRepository.save(bookDetails1);
        bookDetails2 = bookDetailsRepository.save(bookDetails2);

        Directmessage directmessage1 = Directmessage.create(1,"어린왕자 책 구매하고 싶어요.","제곧내", LocalDateTime.now(), profile1.getProfileUid(), profile2.getProfileUid());
        Directmessage directmessage2 = Directmessage.create(1,"글쎄요..","삼만원 쿨거 가능?", LocalDateTime.now(), profile2.getProfileUid(), profile1.getProfileUid());
        Directmessage directmessage3 = Directmessage.create(0,"장난?","삼만원은 너무 비싸요.", LocalDateTime.now(), profile1.getProfileUid(), profile2.getProfileUid());
        directmessage1 = directmessageRepository.save(directmessage1);
        directmessage2 = directmessageRepository.save(directmessage2);
        directmessage3 = directmessageRepository.save(directmessage3);

        Follow follow1 = Follow.create(profile1,profile2.getProfileUid());
        Follow follow2 = Follow.create(profile2,profile1.getProfileUid());
        follow1 = followRepository.save(follow1);
        follow2 = followRepository.save(follow2);

        UserBooks userBooks1 = UserBooks.create(profile1, bookDetails1,0,0);
        UserBooks userBooks2 = UserBooks.create(profile1, bookDetails2,1,0);
        userBooks1 = userBooksRepository.save(userBooks1);
        userBooks2 = userBooksRepository.save(userBooks2);

        Journals journals1 = Journals.create(userBooks1, "책 너무 감동적","인생작",LocalDateTime.now(),"resources/DummyImg/S3BasicImg.jpg","default");
        Journals journals2 = Journals.create(userBooks2, "다른 출판사껀데 이것도 너무 감동적","인생작",LocalDateTime.now(),"resources/DummyImg/S3BasicImg.jpg","default");
        journals1 = journalsRepository.save(journals1);
        journals2 = journalsRepository.save(journals2);
    }

    @Test
    void join() {
        profileService.join("id1",new ProfileDto("다연","imageurl","imgName","안녕하세요","로맨스","호러",null,null,null));
        assertThat(profileRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void deleteOne() throws IOException {
        //Given
        Profile profile1 = profileRepository.findById(profile1Id).get();
        Profile profile2 = profileRepository.findById(profile2Id).get();

        //When
        profileService.deleteOne(profile1.getLogin().getLoginUid());

        //Then
        assertThat(journalsRepository.findAll().size()).isEqualTo(0);
        assertThat(userBooksRepository.findAll().size()).isEqualTo(0);
        assertThat(profileRepository.findById(profile1.getProfileUid())).isEqualTo(Optional.empty());
        assertThat(directmessageRepository.findAllDirectmessagesBySender(-1L).size()).isEqualTo(2);
        assertThat(directmessageRepository.findAllDirectMessagesByRecipient(-1L).size()).isEqualTo(1);
        assertThat(bookDetailsRepository.findAll().size()).isEqualTo(2);
        assertThat(interestsRepository.findAll().size()).isEqualTo(0);
        assertThat(loginRepository.findById("id1")).isEqualTo(Optional.empty());
        assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void getEditProfileFormTest() {
        //when
        ProfileEditResponse profileEditForm = profileService.getProfileEditForm(profile1Id);

        //then
        assertThat(profileEditForm.getNickname()).isEqualTo("콩쥐");
    }

    @Test
    void notExistProfileExceptionTest() {
        assertThrows(NoSuchProfileException.class, () -> {
            profileService.getProfileEditForm(300L);
        });
    }

    @Test
    void editProfileTest() throws IOException {
        //given
        Profile profile = profileRepository.findById(profile1Id).orElseThrow(NoSuchProfileException::new);

        List<String> interests = new LinkedList<>();
        interests.add("로맨스");
        interests.add("스릴러");
        interests.add("판타지");

        FileInputStream fileInputStream = new FileInputStream("src/main/resources/DummyImg/S3BasicImg.jpg");
        MultipartFile multipartFile = new MockMultipartFile("/default/default-profile.png"
                , "S3BasicImg.jpg", "src/main/resources/DummyImg/S3BasicImg.jpg", fileInputStream);

        ProfileEditRequest profileEditRequest = new ProfileEditRequest(multipartFile, "무력 감자", interests);

        //when
        profileService.editProfile(profile.getProfileUid(), profileEditRequest);

        //then
        assertThat(profile.getInterests()).extracting(Interests::getInterestName).contains("로맨스", "스릴러", "판타지");
    }

    @Test
    void getProfileTest() {
        //when
        ProfileGetResponse profile = profileService.getProfile(profile1Id);

        //then
        assertThat(profile.getNickname()).isEqualTo("콩쥐");
    }
}