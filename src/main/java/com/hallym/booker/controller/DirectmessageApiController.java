package com.hallym.booker.controller;

import com.hallym.booker.dto.directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.directmessage.DirectmessageSendRequest;
import com.hallym.booker.service.DirectmessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
