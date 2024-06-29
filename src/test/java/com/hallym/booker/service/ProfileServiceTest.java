package com.hallym.booker.service;

import com.hallym.booker.domain.Login;
import com.hallym.booker.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class ProfileServiceTest {
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    InterestsRepository interestsRepository;
    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    BookDetailsRepository bookDetailsRepository;
    @Autowired
    DirectmessageRepository directmessageRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    UserBooksRepository userBooksRepository;

    @BeforeEach
    void setUp() {
        //Given
        Date now = new Date();
        Login login1 = Login.create("id1","pw1","email1",now);
        login1 = loginRepository.save(login1);
        Login login2 = Login.create("id2","pw2","email2",now);
        login2 = loginRepository.save(login2);

    }

    @Test
    void join() {
    }

    @Test
    void deleteOne() {
    }
}