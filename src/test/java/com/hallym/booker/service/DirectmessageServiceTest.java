package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.directmessage.DirectmessageSendRequest;
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

import java.util.Date;
import java.util.List;

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
        Login loginA = Login.create("gamja", "gamjapw", "gamja@mail.com", new Date(2000, 12, 21));
        loginA = loginRepository.save(loginA);

        Profile profileA = Profile.create(loginA, "감자", "default/default-image.png", "https://default/default-image.png", "무력 감자");
        profileA = profileRepository.save(profileA);
        profileUidA = profileA.getProfileUid();

        Login loginB = Login.create("dubu", "dubupw", "dubu@mail.com", new Date(2000, 07, 03));
        loginB = loginRepository.save(loginB);

        Profile profileB = Profile.create(loginB, "두부", "default/default-image.png", "https://default/default-image.png", "무기력 두부");
        profileB = profileRepository.save(profileB);
        profileUidB = profileB.getProfileUid();
    }

    @Test
    public void directmessageSendTest() {
        //given
        DirectmessageSendRequest directmessageSendRequest = new DirectmessageSendRequest(profileUidA, profileUidB, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");

        //when
        directmessageService.directmessageSend(directmessageSendRequest);

        //then
        List<Directmessage> directmessageList = directmessageRepository.findAll();
        Assertions.assertThat(directmessageList.size()).isEqualTo(1);
    }

    @Test
    public void directmessageSendEx() {
        //given
        DirectmessageSendRequest directmessageSendRequest = new DirectmessageSendRequest(profileUidA, 100L, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");

        //when, then
        assertThrows(NoSuchProfileException.class, (ThrowingRunnable) () -> {
            directmessageService.directmessageSend(directmessageSendRequest);
        });
    }

    @Test
    public void getDirectmessageTest() {
        //given
        DirectmessageSendRequest directmessageSendRequest = new DirectmessageSendRequest(profileUidA, profileUidB, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");
        directmessageService.directmessageSend(directmessageSendRequest);
        List<Directmessage> directmessageList = directmessageRepository.findAll();

        //when
        DirectmessageGetResponse directmessage = directmessageService.getDirectmessage(directmessageList.get(0).getMessageId());

        //then
        Assertions.assertThat(directmessage.getMtitle()).isEqualTo("책 좀 사게 해줘요");
    }

    @Test
    public void deleteDirectmessageTest() {
        //given
        DirectmessageSendRequest directmessageSendRequest = new DirectmessageSendRequest(profileUidA, profileUidB, "책 좀 사게 해줘요", "해리포터 책 사고 싶은데 안될까여..");
        directmessageService.directmessageSend(directmessageSendRequest);
        List<Directmessage> directmessageList = directmessageRepository.findAll();

        //when
        directmessageService.directmessageDelete(directmessageList.get(0).getMessageId());

        //then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchMessageException.class, () -> {
            directmessageService.directmessageDelete(directmessageList.get(0).getMessageId());
        });
    }
}

