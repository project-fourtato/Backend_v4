package com.hallym.booker.repository;

import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Test
    public void testProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id","pw","email", now);
        Login logins = loginRepository.save(login);

        //when
        Profile profile = Profile.create(logins,"nickname","userimageUrl","userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        //then
        Assertions.assertEquals(profiles.getLogin().getLoginUid(), "id");
        assertThat(profiles.getProfileUid()).isNotNull();
        assertThat(profiles.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void testFindById_ProfileUid() {
        //given
        Date now = new Date();
        Login login = Login.create("id","pw","email", now);
        Login logins = loginRepository.save(login);
        Profile profile = Profile.create(logins,"nickname","userimageUrl","userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        //when
        Optional<Profile> optionalProfile = profileRepository.findById(profiles.getProfileUid());
        Profile profile1 = optionalProfile.orElseThrow(() -> new AssertionError("Profile not found"));

        //then
        Assertions.assertEquals(profile1.getProfileUid(), profiles.getProfileUid());
    }

    @Test
    public void testUpdateProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id","pw","email", now);
        Login logins = loginRepository.save(login);
        Profile profile = Profile.create(logins,"nickname","userimageUrl","userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        //when
        profiles.change("newImageUrl", "newImageName", "newMessage");
        profileRepository.save(profiles);
        // profileRepository.flush(); // 필요시 추가

        Optional<Profile> updatedProfile = profileRepository.findById(profiles.getProfileUid());

        //then
        assertThat(updatedProfile).isPresent();

        // 리플렉션을 사용하여 private 필드에 접근
        try {
            Field userImageUrlField = Profile.class.getDeclaredField("userimageUrl");
            userImageUrlField.setAccessible(true);
            String userImageUrl = (String) userImageUrlField.get(updatedProfile.get());

            Field userImageNameField = Profile.class.getDeclaredField("userimageName");
            userImageNameField.setAccessible(true);
            String userImageName = (String) userImageNameField.get(updatedProfile.get());

            Field userMessageField = Profile.class.getDeclaredField("usermessage");
            userMessageField.setAccessible(true);
            String userMessage = (String) userMessageField.get(updatedProfile.get());

            Assertions.assertEquals("newImageUrl", userImageUrl);
            Assertions.assertEquals("newImageName", userImageName);
            Assertions.assertEquals("newMessage", userMessage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Assertions.fail("리플렉션 failed: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id","pw","email", now);
        Login logins = loginRepository.save(login);
        Profile profile = Profile.create(logins,"nickname","userimageUrl","userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        //when
        profileRepository.delete(profiles);
        Optional<Profile> deletedProfile = profileRepository.findById(profiles.getProfileUid());

        //Then
        assertThat(deletedProfile).isEmpty();
    }

    @Test
    public void findAllByNicknameTest() {
        //given
        Date now = new Date();
        Login login = Login.create("id","pw","email", now);
        Login logins = loginRepository.save(login);
        Profile profile = Profile.create(logins,"감자","userimageUrl","userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        Login login2 = Login.create("id2","pw2","email2", now);
        Login logins2 = loginRepository.save(login2);
        Profile profile2 = Profile.create(logins2,"감자감자","userimageUrl","userimageName", "usermessage");
        Profile profiles2 = profileRepository.save(profile2);

        Login login3 = Login.create("id3","pw3","email3", now);
        Login logins3 = loginRepository.save(login3);
        Profile profile3 = Profile.create(logins3,"고구마","userimageUrl","userimageName", "usermessage");
        Profile profiles3 = profileRepository.save(profile3);

        //when
        List<Profile> findNickname = profileRepository.findAllByNickname("감");

        //then
        assertThat(findNickname).extracting(Profile::getNickname).contains("감자", "감자감자");
    }
}
