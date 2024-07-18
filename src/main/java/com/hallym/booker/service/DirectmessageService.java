package com.hallym.booker.service;

import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;

import java.util.List;

public interface DirectmessageService {

    // 쪽지 목록 조회(프로필과 함께)
    List<DirectmessageResponseDTO> getDirectmessageList(Long profileUid);
}
