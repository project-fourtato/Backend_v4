package com.hallym.booker.controller;

import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Login.EffectivenessResponse;
import com.hallym.booker.dto.Login.LoginDto;
import com.hallym.booker.dto.Login.LoginForm;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginApiController {
    private final LoginService loginservice;
    /**
     * 회원 등록
     */
    @PostMapping("/new")
    public ResponseEntity<LoginResponse> loginRegister(@RequestBody @Valid final LoginDto loginDto, HttpServletRequest request){
        Login login = Login.create(loginDto.getUid(), loginDto.getPw(), loginDto.getEmail(), loginDto.getBirth());
        String uid = loginservice.join(login).getLoginUid();
        LoginResponse loginResponse = new LoginResponse(uid);

        //로그인 성공 처리

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession(true);

        //세션에 로그인 회원 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginResponse);

        return ResponseEntity.ok().body(loginResponse);
    }

    /**
     * 아이디 중복검사
     */
    @GetMapping("/checkId/{uid}")
    public ResponseEntity<EffectivenessResponse> idCheck(@PathVariable String uid){
        log.info("이상"+uid);
        Boolean result = loginservice.checkId(uid);
        return ResponseEntity.ok().body(new EffectivenessResponse(result));
    }

    /**
     * 회원 수정 폼
     */
    @PostMapping("/edit")
    public ResponseEntity<LoginDto> idFind(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        Login login = loginservice.findOne(loginResponse.getUid());
        return ResponseEntity.ok().body(new LoginDto(login.getLoginUid(),login.getPw(),login.getEmail(),login.getBirth()));
    }

    /**
     * 회원 수정
     */
    @PutMapping("/edit")
    public ResponseEntity<String> loginEdit(@RequestBody @Valid final LoginDto loginDto, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        loginservice.updateLogin(loginDto.getUid(), loginDto.getPw(), loginDto.getEmail(), loginDto.getBirth());
        return new ResponseEntity("redirection request", HttpStatus.SEE_OTHER);
    }


    /**
     * 회원 로그인
     */
    @PostMapping("")
    public ResponseEntity<LoginResponse> booksState(@RequestBody @Valid final LoginForm loginForm, HttpServletRequest request) {
        Login l = loginservice.loginLogin(loginForm.getId(),loginForm.getPw());
        LoginResponse loginResponse = new LoginResponse(l.getLoginUid());

        //로그인 성공 처리

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession(true);

        //세션에 로그인 회원 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginResponse);

        //profile 생성했는지 확인 후 안했으면 profile 설정 페이지로 넘어가게

        return ResponseEntity.ok().body(loginResponse);

    }

}
