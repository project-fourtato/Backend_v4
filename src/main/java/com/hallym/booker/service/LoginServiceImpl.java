package com.hallym.booker.service;

import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.exception.login.DuplicateProfileException;
import com.hallym.booker.exception.login.NoSuchProfileException;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;

    /**
     *  회원 등록
     */
    @Transactional //변경
    public Login join(Login login){
        validateDuplicateMember(login); //중복 회원 검증
        return loginRepository.save(login);
    }

    private void validateDuplicateMember(Login login) {
        //중복 예외 처리
        if(loginRepository.findById(login.getLoginUid()).orElse(null) != null){
            throw new DuplicateProfileException();
        }
    }

    /**
     * 아이디 중복 검사
     */
    public Boolean checkId(String uid){
        if(loginRepository.findById(uid).orElse(null) != null){
            return false; //회원 존재
        }
        else {
            return true; //회원 없음
        }
    }


    /**
     * 회원 찾기
     */
    public Login findOne(String uid){
        try{
            return loginRepository.findById(uid).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchProfileException();
        }
    }


    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteOne(String uid) {
        try {
            Login login = loginRepository.findById(uid).get();
            loginRepository.delete(login);
        } catch (NoSuchElementException e) {
            throw new NoSuchProfileException();
        }
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateLogin(String uid, String pw, String email, Date birth) {
        try {
            Login findLogin = loginRepository.findById(uid).get();
            findLogin.change(pw, email, birth);
        } catch (NoSuchElementException e) {
            throw new NoSuchProfileException();
        }
    }

    /**
     * 로그인
     */
    @Transactional
    public Login loginLogin(String id, String pw){
        Optional<Login> findLogin = loginRepository.findByLoginUidAndPw(id, pw);
        if (findLogin.isEmpty()){
            throw new NoSuchLoginException();
        }
        //로그인은 했지만 프로필 설정을 안했을 경우
        Profile profile = loginRepository.findById(id).get().getProfile();
        if (profile == null){
            throw new NoSuchProfileException();
        }
        return findLogin.get();
    }
//
//    // foreign key constraint fails 에러를 위한 생쿼리문
//    public void deleteTableWithForeignKeyChecks(Login login) {
//        loginRepository.disableForeignKeyChecks();
//        loginRepository.deleteByUid(login.getUid());
//        loginRepository.enableForeignKeyChecks();
//    }
}
