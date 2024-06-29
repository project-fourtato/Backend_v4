package com.hallym.booker.controller;

import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Login.EffectivenessResponse;
import com.hallym.booker.dto.Login.LoginDto;
import com.hallym.booker.dto.Login.LoginForm;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.dto.Result;
import com.hallym.booker.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final LoginService loginservice;
    /**
     * 회원 등록
     */
    @PostMapping("/login/new")
    public LoginResponse loginRegister(@RequestBody LoginDto loginDto, HttpServletRequest request){
        Login login = Login.create(loginDto.getUid(), loginDto.getPw(), loginDto.getEmail(), loginDto.getBirth());
        loginservice.join(login);
        LoginResponse loginResponse = new LoginResponse(login.getLoginUid());

        //로그인 성공 처리

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession(true);

        //세션에 로그인 회원 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginResponse);

        return loginResponse;

    }

    /**
     * 아이디 중복검사
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/checkId")
    public EffectivenessResponse idCheck(@RequestBody String uid){
        Boolean result = loginservice.checkId(uid);
        return new EffectivenessResponse(result);
    }

    /**
     * 회원 수정 폼
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("login/edit")
    public LoginDto idFind(@RequestBody String uid){
        Login login = loginservice.findOne(uid);
        return new LoginDto(login.getLoginUid(),login.getPw(),login.getEmail(),login.getBirth());
    }

    /**
     * 회원 수정
     */
    @PutMapping("login/edit")
    public ResponseEntity<String> loginEdit(@RequestBody LoginDto request){
        loginservice.updateLogin(request.getUid(), request.getPw(), request.getEmail(), request.getBirth());
        return new ResponseEntity<>("redirection 요청", HttpStatus.FOUND);
    }


    /**
     * 회원 로그인
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login")
    public LoginResponse booksState(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        Login l = loginservice.loginLogin(loginForm.getId(),loginForm.getPw());
        if(l == null){
            return null;
        }
        else{
            LoginResponse loginResponse = new LoginResponse(l.getLoginUid());

            //로그인 성공 처리

            //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
            HttpSession session = request.getSession(true);

            //세션에 로그인 회원 정보 저장
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginResponse);

            return loginResponse;
        }
    }

    //세션 구현하면서 새로 만듦 : sessionstrorage 에서 uid를 받아왔는데 이걸 세션 같이 쓰도록 하자
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("login/id")
    public Result sessionCheck(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return null;
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return null;
        }
        //세션이 유지되면 리턴
        return new Result<>(loginResponse);
    }

}
