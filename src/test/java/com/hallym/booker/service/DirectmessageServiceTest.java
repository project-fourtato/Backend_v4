package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Directmessage.DirectmessageSenderRequest;
import com.hallym.booker.dto.Directmessage.GetDirectmessageResponse;
import com.hallym.booker.exception.directmessage.NoSuchMessageException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.DirectmessageRepository;
import com.hallym.booker.repository.LoginRepository;
import com.hallym.booker.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class DirectmessageServiceTest {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    DirectmessageRepository directmessageRepository;
    @Autowired
    DirectmessageService directmessageService;

    Long profileUidA = 0L;
    Long profileUidB = 0L;

    @Before
    public void setUp() {
        //given
        Login loginA = Login.create("gamjaja", "gamjapw", "gamjaja@mail.com", new Date(2000, 12, 21));
        loginA = loginRepository.save(loginA);

        Profile profileA = Profile.create(loginA, "감자자", "default/default-image.png", "https://default/default-image.png", "무력 감자");
        profileA = profileRepository.save(profileA);
        profileUidA = profileA.getProfileUid();

        Login loginB = Login.create("dububu", "dubupw", "dububu@mail.com", new Date(2000, 07, 03));
        loginB = loginRepository.save(loginB);

        Profile profileB = Profile.create(loginB, "두부부", "default/default-image.png", "https://default/default-image.png", "무기력 두부");
        profileB = profileRepository.save(profileB);
        profileUidB = profileB.getProfileUid();
    }

    @Test
    public void directmessageSendTest() {
        //given
        Profile profile = profileRepository.findById(profileUidB).orElseThrow(NoSuchProfileException::new);
        DirectmessageSenderRequest directmessageSendRequest = new DirectmessageSenderRequest(profileUidA, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");

        //when
        directmessageService.directmessageSend(directmessageSendRequest, profile.getLogin().getLoginUid());

        //then
        List<Directmessage> allDirectmessagesBySender = directmessageRepository.findAllDirectmessagesBySender(profileUidB);
        Assertions.assertThat(allDirectmessagesBySender.size()).isEqualTo(1);
    }

    @Test
    public void directmessageSendEx() {
        //given
        Profile profile = profileRepository.findById(profileUidB).orElseThrow(NoSuchProfileException::new);
        DirectmessageSenderRequest directmessageSendRequest = new DirectmessageSenderRequest(100L, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");

        //when, then
        assertThrows(NoSuchProfileException.class, (ThrowingRunnable) () -> {
            directmessageService.directmessageSend(directmessageSendRequest, profile.getLogin().getLoginUid());
        });
    }

    @Test
    public void getDirectmessageTest() {
        //given
        Profile profile = profileRepository.findById(profileUidB).orElseThrow(NoSuchProfileException::new);
        Directmessage directmessage = Directmessage.create(0, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..", LocalDateTime.now(), profileUidB, profileUidA);
        directmessage = directmessageRepository.save(directmessage);

        //when
        GetDirectmessageResponse getDirectmessage = directmessageService.getDirectmessage(directmessage.getMessageId(), profile.getLogin().getLoginUid());

        //then
        Assertions.assertThat(getDirectmessage.getSenderUid()).isEqualTo(profile.getProfileUid());
    }

    @Test
    public void deleteDirectmessageTest() {
        //given
        Profile profile = profileRepository.findById(profileUidB).orElseThrow(NoSuchProfileException::new);
        Directmessage directmessage = Directmessage.create(0, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..", LocalDateTime.now(), profileUidB, profileUidA);
        directmessage = directmessageRepository.save(directmessage);

        //when
        directmessageService.directmessageDelete(directmessage.getMessageId(), profile.getLogin().getLoginUid());

        //then
        Assertions.assertThat(directmessage.isDeleteSenderCheck()).isEqualTo(true);
    }
}

