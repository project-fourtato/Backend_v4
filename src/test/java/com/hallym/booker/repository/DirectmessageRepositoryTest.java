package com.hallym.booker.repository;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class DirectmessageRepositoryTest {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    DirectmessageRepository directmessageRepository;

    @Test
    @DisplayName("유저 간의 대화 생성 후 senderUid를 통한 조회")
    public void findAllDirectmessagesBySenderTest() {
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Directmessage userAMessage = Directmessage.create(userProfileA, 1, "비가 내리면 여는 상점 책 구매할 수 있을까요?", "도서관에도 책이 없네요,,ㅠ",
                LocalDateTime.now(), userProfileB.getProfileUid());
        Directmessage userAToUserB = directmessageRepository.save(userAMessage);
        Directmessage userBMessage = Directmessage.create(userProfileB, 1, "구매 가능하세요!", "결제 방식 다시 회답 주시면 감사하겠습니다!",
                LocalDateTime.now(), userProfileA.getProfileUid());
        Directmessage userBToUserA = directmessageRepository.save(userBMessage);
        Directmessage userAMessageRe = Directmessage.create(userProfileA, 0, "계좌번호 주시면 입금하겠습니다!", "감사합니다!",
                LocalDateTime.now(), userProfileB.getProfileUid());
        Directmessage userAToUserBRe = directmessageRepository.save(userAMessageRe);

        //when
        List<Directmessage> allDirectmessagesBySender = directmessageRepository.findAllDirectmessagesBySender(userProfileA.getProfileUid());

        //then
        assertThat(allDirectmessagesBySender).extracting(Directmessage::getMtitle).contains(userAToUserB.getMtitle(), userAToUserBRe.getMtitle());
    }

    @Test
    @DisplayName("유저 간의 대화 생성 후 Recipient를 통한 조회")
    public void findAllDirectMessagesByRecipientTest() {
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Directmessage userAMessage = Directmessage.create(userProfileA, 1, "비가 내리면 여는 상점 책 구매할 수 있을까요?", "도서관에도 책이 없네요,,ㅠ",
                LocalDateTime.now(), userProfileB.getProfileUid());
        Directmessage userAToUserB = directmessageRepository.save(userAMessage);
        Directmessage userBMessage = Directmessage.create(userProfileB, 1, "구매 가능하세요!", "결제 방식 다시 회답 주시면 감사하겠습니다!",
                LocalDateTime.now(), userProfileA.getProfileUid());
        Directmessage userBToUserA = directmessageRepository.save(userBMessage);
        Directmessage userAMessageRe = Directmessage.create(userProfileA, 0, "계좌번호 주시면 입금하겠습니다!", "감사합니다!",
                LocalDateTime.now(), userProfileB.getProfileUid());
        Directmessage userAToUserBRe = directmessageRepository.save(userAMessageRe);

        //when
        List<Directmessage> allDirectMessagesByRecipient = directmessageRepository.findAllDirectMessagesByRecipient(userProfileB.getProfileUid());

        //then
        assertThat(allDirectMessagesByRecipient).extracting(Directmessage::getProfile).extracting(Profile::getNickname).contains("gamjaa");
    }

}