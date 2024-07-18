package com.hallym.booker.repository;

import com.hallym.booker.domain.Follow;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import org.junit.Test;
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
public class FollowRepositoryTest {
    @Autowired
    FollowRepository followRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;

    // from_user_id : 나
    // to_user_id : 인플루언서
    @Test
    public void findByFromUserIdAndToUserIdTest() {
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Follow follow = Follow.create(userProfileA, userProfileB.getProfileUid()); // from_user_id : gamja, to_user_id : goguma, gamja -> goguma
        Follow followUser = followRepository.save(follow);

        //when
        Optional<Follow> byFromUserIdAndToUserId = followRepository.findByFromUserIdAndToUserId(followUser.getProfile().getProfileUid(), followUser.getToUserId());

        //then
        if(byFromUserIdAndToUserId.isPresent()) {
            assertThat(byFromUserIdAndToUserId.get().getProfile().getNickname()).isEqualTo("gamjaa");
        } else {
            System.out.println("value does not exist");
        }
    }

//    @Test
//    @DisplayName("fromUserId로 follow 객체 조회, 객체 카운팅 쿼리 테스트")
//    public void findAllByFromUserIdAndCountTest() {
//        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
//        Login userALogin = loginRepository.save(loginA);
//        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
//        Profile userProfileA = profileRepository.save(profileA);
//
//        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
//        Login userBLogin = loginRepository.save(loginB);
//        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
//        Profile userProfileB = profileRepository.save(profileB);
//
//        Login loginC = Login.create("tofu", "totofu", "to@gamja.com", new Date(2003, 02, 03));
//        Login userCLogin = loginRepository.save(loginC);
//        Profile profileC = Profile.create(userCLogin, "tofu", "https://tofusprofileimage.com", "tofusprofileimage", "무력 두부");
//        Profile userProfileC = profileRepository.save(profileC);
//
//        Follow followGamjaGoguma = Follow.create(userProfileA, userProfileB.getProfileUid()); // to_user_id : 인플루언서, from_user_id : 나, from -> to
//        Follow gamjaToGoguma = followRepository.save(followGamjaGoguma);
//        Follow followGamjaTofu = Follow.create(userProfileA, userProfileC.getProfileUid());
//        Follow gamjaToTofu = followRepository.save(followGamjaTofu);
//
//        //when
//        List<Follow> allByFromUserId = followRepository.findAllByFromUserId(gamjaToGoguma.getProfile().getProfileUid());
//        Long countToUser = followRepository.countByFromUserId(gamjaToGoguma.getProfile().getProfileUid());
//
//        //then
//        assertThat(allByFromUserId).extracting(Follow::getToUserId).contains(gamjaToGoguma.getToUserId(), gamjaToTofu.getToUserId());
//        assertThat(countToUser).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("toUserId로 follow 객체 조회, 객체 카운팅 쿼리 테스트")
//    public void findAllByToUserIdTest() {
//        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
//        Login userALogin = loginRepository.save(loginA);
//        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
//        Profile userProfileA = profileRepository.save(profileA);
//
//        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 7, 03));
//        Login userBLogin = loginRepository.save(loginB);
//        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
//        Profile userProfileB = profileRepository.save(profileB);
//
//        Login loginC = Login.create("tofu", "totofu", "to@gamja.com", new Date(2003, 2, 03));
//        Login userCLogin = loginRepository.save(loginC);
//        Profile profileC = Profile.create(userCLogin, "tofu", "https://tofusprofileimage.com", "tofusprofileimage", "무력 두부");
//        Profile userProfileC = profileRepository.save(profileC);
//
//        Login loginD = Login.create("tomato", "totomato", "toma@gamja.com", new Date(2007, 8, 14));
//        Login userDLogin = loginRepository.save(loginD);
//        Profile profileD = Profile.create(userDLogin, "tomato", "https://tomatosprofileimage.com", "tomatosprofileimage", "무력 토마토");
//        Profile userProfileD = profileRepository.save(profileD);
//
//        Follow followGamjaGoguma = Follow.create(userProfileB, userProfileA.getProfileUid()); // to_user_id : 인플루언서, from_user_id : 나, from -> to
//        Follow gogumaToGamja = followRepository.save(followGamjaGoguma);
//        Follow followTofuGamja = Follow.create(userProfileC, userProfileA.getProfileUid());
//        followRepository.save(followTofuGamja);
//        Follow followTomatoGamja = Follow.create(userProfileD, userProfileA.getProfileUid());
//        followRepository.save(followTomatoGamja);
//
//        //when
//        List<Follow> allByToUserId = followRepository.findAllByToUserId(gogumaToGamja.getToUserId());
//        Long countFromUser = followRepository.countByToUserId(gogumaToGamja.getToUserId());
//
//        //then
//        assertThat(allByToUserId).extracting(Follow::getProfile).extracting(Profile::getNickname).contains("gogumaa", "tofu", "tomato");
//        assertThat(countFromUser).isEqualTo(3);
//    }
}