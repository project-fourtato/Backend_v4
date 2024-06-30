package com.hallym.booker.repository;

import com.hallym.booker.domain.Followers;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowersRepositoryTest {
    @Autowired
    FollowersRepository followersRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    public void findByProfile_ProfileUidAndAndFollowerUid() {
        //Given
        Login loginA = Login.create("gamja", "gamgamja", "gam@gamja.com", new Date(2000, 12, 21));
        Login userALogin = loginRepository.save(loginA);
        Profile profileA = Profile.create(userALogin, "gamjaa", "https://gmajasprofileimage.com", "gmajasprofileimage", "무력 감자");
        Profile userProfileA = profileRepository.save(profileA);

        Login loginB = Login.create("goguma", "gogoguma", "go@gamja.com", new Date(2000, 07, 03));
        Login userBLogin = loginRepository.save(loginB);
        Profile profileB = Profile.create(userBLogin, "gogumaa", "https://gogumasprofileimage.com", "gogumasprofileimage", "무력 고구마");
        Profile userProfileB = profileRepository.save(profileB);

        Followers followers = Followers.create(userProfileA, userProfileB.getProfileUid());
        Followers followersUser = followersRepository.save(followers);

        //when
        Optional<Followers> byFromUserIdAndToUserId = followersRepository.findByProfile_ProfileUidAndAndFollowerUid(followersUser.getProfile().getProfileUid(), followersUser.getFollowerUid());

        //then
        if(byFromUserIdAndToUserId.isPresent()) {
            assertThat(byFromUserIdAndToUserId.get().getProfile().getNickname()).isEqualTo("gamjaa");
        } else {
            System.out.println("value does not exist");
        }
    }

    @Test
    @DisplayName("followers 조회, 객체 카운팅 쿼리 테스트")
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

        Followers followersGamjaGoguma = Followers.create(userProfileA, userProfileB.getProfileUid());
        Followers gamjaToGoguma = followersRepository.save(followersGamjaGoguma);
        Followers followersGamjaTofu = Followers.create(userProfileA, userProfileC.getProfileUid());
        Followers gamjaToTofu = followersRepository.save(followersGamjaTofu);

        //when
        List<Followers> followers = followersRepository.findByProfile_ProfileUid(userProfileA.getProfileUid());
        Long cnt = followersRepository.countByProfile_ProfileUid(userProfileA.getProfileUid());

        //then
        assertThat(followers).extracting(Followers::getFollowerUid).contains(gamjaToGoguma.getFollowerUid(), gamjaToTofu.getFollowerUid());
        assertThat(cnt).isEqualTo(2);
    }
}
