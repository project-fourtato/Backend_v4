package com.hallym.booker.controller;

import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.service.DirectmessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Directmessages")
public class DirectmessageController {

    @Autowired
    private DirectmessageService directmessageService;

    // 쪽지 목록 조회(프로필과 함께) API
    @GetMapping("/DirectmessagesList/{profileUid}")
    public ResponseEntity<Map<String, List<DirectmessageResponseDTO>>> getDirectmessageList(@PathVariable Long profileUid) {
        List<DirectmessageResponseDTO> directmessageList = directmessageService.getDirectmessageList(profileUid);

        Map<String, List<DirectmessageResponseDTO>> response = new HashMap<>();
        response.put("data", directmessageList);

        return ResponseEntity.ok().body(response);
    }
}
