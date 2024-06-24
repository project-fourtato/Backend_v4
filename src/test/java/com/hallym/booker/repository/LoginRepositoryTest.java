package com.hallym.booker.repository;

import com.hallym.booker.domain.Login;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class LoginRepositoryTest {
    @Autowired
    private LoginRepository loginRepository;
    @Test
    void findByLoginUidAndPw() {
        //Given
        Date now = new Date();
        Login login = Login.create("id","pw","email",now);
        Login login_0 = loginRepository.save(login);
        //When
        Optional<Login> login1 =  loginRepository.findByLoginUidAndPw("id","pw");
        Optional<Login> login2 =  loginRepository.findByLoginUidAndPw("id","wrong_pw");
        //Then
        Assertions.assertThat(login1.get().getLoginUid()).isEqualTo("id");
        Assertions.assertThat(login2.isEmpty()).isEqualTo(true);
    }
}