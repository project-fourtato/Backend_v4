package com.hallym.booker.repository;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.hallym.booker.repository.InterestsRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class InterestsRepositoryTest {
    @Autowired
    private InterestsRepository interestsRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Test
    public void testInterests(){
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login1 = loginRepository.save(login);
        Profile profile = Profile.create(login1,"닉네임","imageurl","imagename", "안녕하세요");
        Profile profile1 = profileRepository.save(profile);
        Interests interests = Interests.create("hi", profile1 );
        //When
        Interests interests1 = interestsRepository.save(interests);
        //Then
        Assertions.assertEquals(interests1.getInterestName(), "hi");
    }

    @Test
    public void test_findByProfile_ProfileUid(){
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login1 = loginRepository.save(login);
        Profile profile = Profile.create(login1,"닉네임","imageurl","imagename", "안녕하세요");
        Profile profile1 = profileRepository.save(profile);
        Interests interests1 = Interests.create("호러", profile1 );
        interestsRepository.save(interests1);
        Interests interests2 = Interests.create("로맨스", profile1 );
        interestsRepository.save(interests2);
        //When
        List<Interests> interestsList= interestsRepository.findByProfile_ProfileUid(profile1.getProfileUid());
        //Then
        assertThat(interestsList.size()).isEqualTo(2);
    }

    @Test
    public void test_findByProfile_ProfileUidNotIn(){
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        Profile profile = Profile.create(login_0,"닉네임","imageurl","imagename", "안녕하세요");
        profileRepository.save(profile);

        Login login2 = Login.create("id2","pw2","email2",now);
        Login login_2 = loginRepository.save(login2);
        Profile profile2 = Profile.create(login_2,"닉네임2","imageurl2","imagename2", "hi");
        profileRepository.save(profile2);

        Interests interests1 = Interests.create("호러", profile );
        interestsRepository.save(interests1);
        Interests interests2 = Interests.create("로맨스", profile );
        interestsRepository.save(interests2);
        Interests interests3 = Interests.create("판타지", profile2 );
        interestsRepository.save(interests3);

        //When
        List<Interests> interestsList = interestsRepository.findByProfile_ProfileUidNotIn(profile2.getProfileUid());
        //Then
        assertThat(interestsList).extracting(Interests::getInterestName).contains("호러");
    }

    @Test
    public void deleteAllByProfile_ProfileUidTest() {
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        Profile profile = Profile.create(login_0,"닉네임","imageurl","imagename", "안녕하세요");
        profileRepository.save(profile);

        Login login2 = Login.create("id2","pw2","email2",now);
        Login login_2 = loginRepository.save(login2);
        Profile profile2 = Profile.create(login_2,"닉네임2","imageurl2","imagename2", "hi");
        profileRepository.save(profile2);

        Interests interests1 = Interests.create("호러", profile );
        interestsRepository.save(interests1);
        Interests interests2 = Interests.create("로맨스", profile );
        interestsRepository.save(interests2);
        Interests interests3 = Interests.create("판타지", profile2 );
        interestsRepository.save(interests3);

        //when
        List<Interests> interestsList = new ArrayList<>(profile.getInterests());

        for (Interests interests : interestsList) {
            interests.removeProfile(profile);
        }
        interestsRepository.deleteAllByProfile_ProfileUid(profile.getProfileUid());

        //then
        assertThat(profile.getInterests().size()).isEqualTo(0);
    }

    @Test
    public void findSameInterestProfile() {
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        Profile profile = Profile.create(login_0,"닉네임","imageurl","imagename", "안녕하세요");
        profileRepository.save(profile);

        Login login2 = Login.create("id2","pw2","email2",now);
        Login login_2 = loginRepository.save(login2);
        Profile profile2 = Profile.create(login_2,"닉네임2","imageurl2","imagename2", "hi");
        profileRepository.save(profile2);

        Login login3 = Login.create("id3","pw3","email3",now);
        Login login_3 = loginRepository.save(login3);
        Profile profile3 = Profile.create(login_3,"닉네임3","imageurl3","imagename3", "hello");
        profileRepository.save(profile3);

        Interests interests1 = Interests.create("호러", profile );
        interestsRepository.save(interests1);
        Interests interests2 = Interests.create("로맨스", profile );
        interestsRepository.save(interests2);
        Interests interests3 = Interests.create("스릴러", profile);
        interestsRepository.save(interests3);
        Interests interests4 = Interests.create("판타지", profile2 );
        interestsRepository.save(interests4);
        Interests interests5 = Interests.create("로맨스", profile3);
        interestsRepository.save(interests5);
        Interests interests6 = Interests.create("스릴러", profile3);
        interestsRepository.save(interests6);

        //when
        List<Profile> sameInterestProfile = interestsRepository.findSameInterestProfile(profile.getProfileUid());

        //then
        assertThat(sameInterestProfile).extracting(Profile::getProfileUid).contains(profile3.getProfileUid(), profile.getProfileUid());
    }
}