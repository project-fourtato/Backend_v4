package com.hallym.booker.controller;

import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Interests.InterestsResponseDTO;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.service.InterestsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Interests")
public class InterestsController {

    private final InterestsService interestsService;

    // 특정 프로필에 대한 모든 관심사 조회 API
    @GetMapping("/{profileUid}")
    public ResponseEntity<List<InterestsResponseDTO>> getInterestsByProfile(HttpServletRequest request) {

        // 세션 확인 코드 추가
        HttpSession session = request.getSession(false);
        if (session == null) { // 세션이 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) { // 세션에 회원 데이터가 없으면 홈으로 이동
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        // 기존 로직
        List<InterestsResponseDTO> interests = interestsService.getInterestsByProfile(loginResponse.getUid());
        return ResponseEntity.ok().body(interests);
    }

    // 모든 관심사들 조회(본인 제외) API
    @GetMapping("/interests/allExcept/{uid}")
    public ResponseEntity<List<InterestsResponseDTO>> getAllInterestsExceptProfile(@PathVariable("uid") Long profileUid) {
        List<InterestsResponseDTO> interests = interestsService.getAllInterestsExceptProfile(profileUid);
        return ResponseEntity.ok().body(interests);
    }

}
