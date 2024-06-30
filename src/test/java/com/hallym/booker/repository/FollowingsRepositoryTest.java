package com.hallym.booker.repository;

import com.hallym.booker.domain.Followings;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowingsRepositoryTest {
    @Autowired
    FollowingsRepository followingsRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    public void findByProfile_ProfileUidAndAndFollowingUid() {
        //Given
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Followings followings = Followings.create(userProfileA, userProfileB.getProfileUid());
        Followings followingsUser = followingsRepository.save(followings);

        //when
        Optional<Followings> byFromUserIdAndToUserId = followingsRepository.findByProfile_ProfileUidAndAndFollowingUid(followingsUser.getProfile().getProfileUid(), followingsUser.getFollowingUid());

        //then
        if(byFromUserIdAndToUserId.isPresent()) {
            assertThat(byFromUserIdAndToUserId.get().getProfile().getNickname()).isEqualTo("gamjaa");
        } else {
            System.out.println("value does not exist");
        }
    }

    @Test
    @DisplayName("fromUserId로 follow 객체 조회, 객체 카운팅 쿼리 테스트")
    public void findByProfile_ProfileUidAndCountTest() {
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Login loginC = Login.create("tofu", "totofu", "to@gamja.com", new Date(2003, 02, 03));
        Login userCLogin = loginRepository.save(loginC);
        Profile profileC = Profile.create(userCLogin, "tofu", "https://tofusprofileimage.com", "tofusprofileimage", "무력 두부");
        Profile userProfileC = profileRepository.save(profileC);

        Followings followingsGamjaGoguma = Followings.create(userProfileA, userProfileB.getProfileUid());
        Followings gamjaToGoguma = followingsRepository.save(followingsGamjaGoguma);
        Followings followingsGamjaTofu = Followings.create(userProfileA, userProfileC.getProfileUid());
        Followings gamjaToTofu = followingsRepository.save(followingsGamjaTofu);

        //when
        List<Followings> followings = followingsRepository.findByProfile_ProfileUid(userProfileA.getProfileUid());
        Long cnt = followingsRepository.countByProfile_ProfileUid(userProfileA.getProfileUid());

        //then
        assertThat(followings).extracting(Followings::getFollowingUid).contains(gamjaToGoguma.getFollowingUid(), gamjaToTofu.getFollowingUid());
        assertThat(cnt).isEqualTo(2);
    }

}