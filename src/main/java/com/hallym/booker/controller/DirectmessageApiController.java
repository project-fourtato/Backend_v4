package com.hallym.booker.controller;

import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Directmessage.DirectmessageResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSenderRequest;
import com.hallym.booker.dto.Directmessage.GetDirectmessageResponse;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.service.DirectmessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DirectmessageApiController {
    private final DirectmessageService directmessageService;

    /**
     * 쪽지 등록
     */
    @PostMapping("/directmessages/new")
    public ResponseEntity<String> directmessageSend(@RequestBody DirectmessageSenderRequest directmessageSendRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        directmessageService.directmessageSend(directmessageSendRequest, loginResponse.getUid());
        return new ResponseEntity<>("Directmessage Send Success", HttpStatus.OK);
    }

    /**
     * 쪽지 조회
     */
    @GetMapping("/directmessages/{messageId}")
    public ResponseEntity<GetDirectmessageResponse> getDirecmessage(@PathVariable Long messageId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        return ResponseEntity.ok().body(directmessageService.getDirectmessage(messageId, loginResponse.getUid()));
    }

    // 쪽지 목록 조회(프로필과 함께) API (내가 받은 메세지)
    @GetMapping("/directmessages/DirectmessagesList/{userCheck}")
    public ResponseEntity<Map<String, List<DirectmessageResponse>>> getDirectmessageList(HttpServletRequest request, @PathVariable("userCheck") String userCheck) {

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
        List<DirectmessageResponse> directmessageList = directmessageService.getDirectmessageList(loginResponse.getUid(), userCheck);

        Map<String, List<DirectmessageResponse>> response = new HashMap<>();
        response.put("data", directmessageList);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 쪽지 삭제
     */
    @PostMapping("/directmessages/{messageId}/delete")
    public ResponseEntity<String> directmessageDelete(@PathVariable Long messageId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        directmessageService.directmessageDelete(messageId, loginResponse.getUid());
        return new ResponseEntity<>("Directmessages deleted successfully", HttpStatus.OK);
    }

    // 쪽지 상태 업데이트
    @PutMapping("/directmessages/mcheckUpdate/{messageid}")
    public ResponseEntity<String> updateMcheck(@PathVariable Long messageid) {
        directmessageService.updateMcheck(messageid, 1);
        return ResponseEntity.ok().body("mcheck update success");
    }

}