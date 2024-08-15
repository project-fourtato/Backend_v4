package com.hallym.booker.service;

import com.hallym.booker.dto.Profile.*;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    /**
     *  프로필 등록
     */
    void join(String loginUid, ProfileDto profileDto);

    /**
     * 회원 삭제
     */
    void deleteOne(String loginUid) throws IOException;

    /**
     * 프로필 수정 폼
     */
    ProfileEditResponse getProfileEditForm(String loginId);

    /**
     * 프로필 수정
     */
    void editProfile(String loginId, ProfileEditRequest profileEdit) throws IOException;

    /**
     * 프로필 조회
     */
    ProfileGetResponse getProfile(String loginId);

    /**
     * 관심사가 동일한 프로필 목록 조회
     */
    SameAllInterestProfileResponse getProfileSameInterests(String loginId);

    /**
     * 유저 검색에서 유저 닉네임을 통해 조회
     */
    List<SearchNicknameResultResponse> serachNickname(String nickname);

    /**
     * 닉네임 중복 확인
     */
    void duplicateNickname(String nickname);
}
