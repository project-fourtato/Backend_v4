package com.hallym.booker.repository;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        org.assertj.core.api.Assertions.assertThat(interestsList.size()).isEqualTo(2);
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
        List<Interests> interestsList= interestsRepository.findByProfile_ProfileUidNotIn(profile2.getProfileUid());
        //Then
        org.assertj.core.api.Assertions.assertThat(interestsList.size()).isEqualTo(2);
    }
}