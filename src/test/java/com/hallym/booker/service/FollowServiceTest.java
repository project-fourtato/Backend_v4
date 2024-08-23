package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.exception.login.NoSuchProfileException;
import com.hallym.booker.repository.*;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class FollowServiceTest {
    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    BookDetailsRepository bookDetailsRepository;
    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    UserBooksRepository userBooksRepository;
    @Before
    public void setUp() throws Exception {
        //Given
        Date now = new Date();
        Login login1 = Login.create("id1","pw1","email1",now);
        login1 = loginRepository.save(login1);
        Login login2 = Login.create("id2","pw2","email2",now);
        login2 = loginRepository.save(login2);
        Login login3 = Login.create("id3","pw3","email3",now);
        login2 = loginRepository.save(login3);
    }

    @Test
    public void newFollow() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        List<Follow> allByFromUserId = followRepository.findAllByFromUserId(profile1.getProfileUid());

        Assertions.assertThat(allByFromUserId).extracting(Follow::getToUserId).contains(profile2.getProfileUid());
    }

    @Test
    public void findAllToUserIdProfile() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);
        Profile profile3 = Profile.create(loginRepository.findById("id3").get(),"쥐돌이","resources/DummyImg/S3BasicImg.jpg","default","찍찍");
        profile3 = profileRepository.save(profile3);
        String loginId = profileRepository.findById(profile1.getProfileUid()).orElseThrow(NoSuchProfileException::new).getLogin().getLoginUid();

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        followService.newFollow(profile2.getProfileUid(), profile1.getProfileUid()); //profile2 -> profile1
        followService.newFollow(profile1.getProfileUid(), profile3.getProfileUid()); //profile1 -> profile3

        Assertions.assertThat(followService.findAllToUserIdProfile(loginId).size()).isEqualTo(2);
    }

    @Test
    public void findAllFromUserIdProfile() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);
        Profile profile3 = Profile.create(loginRepository.findById("id3").get(),"쥐돌이","resources/DummyImg/S3BasicImg.jpg","default","찍찍");
        profile3 = profileRepository.save(profile3);
        String loginId = profileRepository.findById(profile1.getProfileUid()).orElseThrow(NoSuchProfileException::new).getLogin().getLoginUid();

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        followService.newFollow(profile2.getProfileUid(), profile1.getProfileUid()); //profile2 -> profile1
        followService.newFollow(profile1.getProfileUid(), profile3.getProfileUid()); //profile1 -> profile3

        Assertions.assertThat(followService.findAllFromUserIdProfile(loginId).size()).isEqualTo(1);
    }

    @Test
    public void findFollowingsLatestJournals() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);
        Profile profile3 = Profile.create(loginRepository.findById("id3").get(),"쥐돌이","resources/DummyImg/S3BasicImg.jpg","default","찍찍");
        profile3 = profileRepository.save(profile3);
        String loginId = profileRepository.findById(profile1.getProfileUid()).orElseThrow(NoSuchProfileException::new).getLogin().getLoginUid();

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        followService.newFollow(profile2.getProfileUid(), profile1.getProfileUid()); //profile2 -> profile1
        followService.newFollow(profile1.getProfileUid(), profile3.getProfileUid()); //profile1 -> profile3

        BookDetails bookDetails2 = BookDetails.create("isbn","어린왕자","dy","dy","url");
        bookDetails2 = bookDetailsRepository.save(bookDetails2);
        UserBooks userBooks2 = UserBooks.create(profile2,bookDetails2,0,0);
        userBooks2 = userBooksRepository.save(userBooks2);
        Journals journals = Journals.create(userBooks2,"어린왕자를 읽고..","쌸라쌸라", LocalDateTime.now(),"url","name");
        journalsRepository.save(journals);
        Journals journals2 = Journals.create(userBooks2,"백설공주를 읽고..","쌸라쌸라",LocalDateTime.of(2025,1,1,1,1),"url","name");
        journalsRepository.save(journals2);

        int cnt = followService.findFollowingsLatestJournals(loginId).size();
        Assertions.assertThat(cnt).isEqualTo(2);
    }

    @Test
    public void removeFollowing() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);
        Profile profile3 = Profile.create(loginRepository.findById("id3").get(),"쥐돌이","resources/DummyImg/S3BasicImg.jpg","default","찍찍");
        profile3 = profileRepository.save(profile3);
        String loginId = profileRepository.findById(profile1.getProfileUid()).orElseThrow(NoSuchProfileException::new).getLogin().getLoginUid();

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        followService.newFollow(profile2.getProfileUid(), profile1.getProfileUid()); //profile2 -> profile1
        followService.newFollow(profile1.getProfileUid(), profile3.getProfileUid()); //profile1 -> profile3

        followService.removeFollowing(profile1.getProfileUid(),profile2.getProfileUid());
        Assertions.assertThat(followService.findAllToUserIdProfile(loginId).size()).isEqualTo(1);
    }

    @Test
    public void checkFollowing() {
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2

        Assertions.assertThat(followService.checkFollowing(profile1.getProfileUid(),profile2.getProfileUid())).isEqualTo(true);
        Assertions.assertThat(followService.checkFollowing(profile2.getProfileUid(),profile1.getProfileUid())).isEqualTo(false);
    }

    @Test
    public void 팔로우_팔로잉_수_조회(){
        Profile profile1 = Profile.create(loginRepository.findById("id1").get(),"콩쥐","resources/DummyImg/S3BasicImg.jpg","default","안녕하세요 전 소설 좋아해요");
        profile1 = profileRepository.save(profile1);
        Profile profile2 = Profile.create(loginRepository.findById("id2").get(),"팥쥐","resources/DummyImg/S3BasicImg.jpg","default","책 안좋아해요");
        profile2 = profileRepository.save(profile2);
        Profile profile3 = Profile.create(loginRepository.findById("id3").get(),"쥐돌이","resources/DummyImg/S3BasicImg.jpg","default","찍찍");
        profile3 = profileRepository.save(profile3);

        followService.newFollow(profile1.getProfileUid(), profile2.getProfileUid()); //profile1 -> profile2
        followService.newFollow(profile2.getProfileUid(), profile1.getProfileUid()); //profile2 -> profile1
        followService.newFollow(profile1.getProfileUid(), profile3.getProfileUid()); //profile1 -> profile3

        Assertions.assertThat(profile1.getCountFollowings()).isEqualTo(2);
        Assertions.assertThat(profile1.getCountFollowers()).isEqualTo(1);

        followService.removeFollowing(profile1.getProfileUid(),profile2.getProfileUid());
        Assertions.assertThat(profile1.getCountFollowings()).isEqualTo(1);
        Assertions.assertThat(profile2.getCountFollowers()).isEqualTo(0);
    }
}