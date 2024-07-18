package com.hallym.booker.service;

import com.hallym.booker.domain.Login;
import com.hallym.booker.exception.login.DuplicateProfileException;
import com.hallym.booker.exception.login.NoSuchProfileException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface LoginService {
    /**
     *  회원 등록
     */
    void join(Login login);

    /**
     * 아이디 중복 검사
     */
    Boolean checkId(String uid);

    /**
     * 회원 찾기
     */
    Login findOne(String uid);

    /**
     * 회원 삭제
     */
    void deleteOne(String uid);

    /**
     * 회원 수정
     */
    void updateLogin(String uid, String pw, String email, Date birth);

    /**
     * 로그인
     */
    Login loginLogin(String id, String pw);
}
