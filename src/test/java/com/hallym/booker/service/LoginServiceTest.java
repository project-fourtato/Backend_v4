package com.hallym.booker.service;

import com.hallym.booker.domain.Login;
import com.hallym.booker.exception.login.DuplicateProfileException;
import com.hallym.booker.exception.login.NoSuchProfileException;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.repository.LoginRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
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
class LoginServiceTest {
    @Autowired
    LoginService loginService;
    @Autowired
    LoginRepository loginRepository;
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
        Date now = new Date();
        Login new_login = Login.create("newId","newPw","newEmail",now);
        loginService.join(new_login);
        Login check = loginRepository.findById(new_login.getLoginUid()).get();
        Assertions.assertThat(new_login.getLoginUid()).isEqualTo(check.getLoginUid());
    }

    @Test
    void 이미등록된회원일경우(){
        Date now = new Date();
        Login new_login = Login.create("id1","newPw","newEmail",now);
        assertThrows(DuplicateProfileException.class,
                ()-> loginService.join(new_login));
    }

    @Test
    void checkId() {
        Boolean result1 = loginService.checkId("id1");
        Assertions.assertThat(result1).isEqualTo(false);
        Boolean result2 = loginService.checkId("newId");
        Assertions.assertThat(result2).isEqualTo(true);

    }

    @Test
    void findOne() {
        Login login = loginService.findOne("id1");
        Assertions.assertThat(login.getPw()).isEqualTo("pw1");
    }

    @Test
    void deleteOne() {
        loginService.deleteOne("id1");
        assertThrows(NoSuchProfileException.class, ()->
                loginService.findOne("id1"));
    }

    @Test
    void updateLogin() {
        Login login = loginService.findOne("id1");
        loginService.updateLogin(login.getLoginUid(),"changePw","changeEmail",login.getBirth());
        Login changeLogin = loginService.findOne("id1");
        Assertions.assertThat("changePw").isEqualTo(changeLogin.getPw());
    }

    @Test
    void loginLogin() {
        assertThrows(NoSuchLoginException.class,
                ()-> loginService.loginLogin("id1","pw2"));
    }

    @Test
    void 로그인실패(){
        assertThrows(NoSuchLoginException.class,
                ()-> loginService.loginLogin("id1","pw2"));
    }
}