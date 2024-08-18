package com.hallym.booker.service;

import com.hallym.booker.dto.Interests.InterestsResponseDTO;

import java.util.List;

public interface InterestsService {
    // 특정 프로필에 대한 모든 관심사 조회
    List<String> getInterestsByProfile(String loginUid);

}