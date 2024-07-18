package com.hallym.booker.controller;

import com.hallym.booker.dto.Interests.InterestsResponseDTO;
import com.hallym.booker.service.InterestsService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<InterestsResponseDTO>> getInterestsByProfile(@PathVariable("profileUid") Long profileUid) {
        List<InterestsResponseDTO> interests = interestsService.getInterestsByProfile(profileUid);
        return ResponseEntity.ok().body(interests);
    }

    // 모든 관심사들 조회(본인 제외) API
    @GetMapping("/interests/allExcept/{uid}")
    public ResponseEntity<List<InterestsResponseDTO>> getAllInterestsExceptProfile(@PathVariable("uid") Long profileUid) {
        List<InterestsResponseDTO> interests = interestsService.getAllInterestsExceptProfile(profileUid);
        return ResponseEntity.ok().body(interests);
    }

}
