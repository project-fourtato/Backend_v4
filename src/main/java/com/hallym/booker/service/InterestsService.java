package com.hallym.booker.service;

import com.hallym.booker.dto.Interests.InterestsResponseDTO;

import java.util.List;

public interface InterestsService {
    // 특정 프로필에 대한 모든 관심사 조회
    List<InterestsResponseDTO> getInterestsByProfile(String loginUid);

    // 모든 관심사들 조회(본인 제외)
    List<InterestsResponseDTO> getAllInterestsExceptProfile(Long profileUid);
}