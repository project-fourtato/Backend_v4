package com.hallym.booker.controller;

import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.dto.Directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSendRequest;
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
    public ResponseEntity<String> directmessageSend(@RequestBody DirectmessageSendRequest request) {
        directmessageService.directmessageSend(request);
        return new ResponseEntity<>("Directmessage Send Success", HttpStatus.OK);
    }

    /**
     * 쪽지 조회
     */
    @GetMapping("/directmessages/{messageId}")
    public DirectmessageGetResponse getDirecmessage(@PathVariable Long messageId) {
        return directmessageService.getDirectmessage(messageId);
    }

    // 쪽지 목록 조회(프로필과 함께) API
    @GetMapping("/directmessages/DirectmessagesList/{profileUid}")
    public ResponseEntity<Map<String, List<DirectmessageResponseDTO>>> getDirectmessageList(HttpServletRequest request) {

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
        List<DirectmessageResponseDTO> directmessageList = directmessageService.getDirectmessageList(loginResponse.getUid());

        Map<String, List<DirectmessageResponseDTO>> response = new HashMap<>();
        response.put("data", directmessageList);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 쪽지 삭제
     */
    @PostMapping("/directmessages/{messageId}/delete")
    public ResponseEntity<String> directmessageDelete(@PathVariable Long messageId) {
        directmessageService.directmessageDelete(messageId);
        return new ResponseEntity<>("Directmessages deleted successfully", HttpStatus.OK);
    }
}