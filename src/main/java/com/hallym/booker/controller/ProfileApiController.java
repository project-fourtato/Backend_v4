package com.hallym.booker.controller;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.dto.Profile.*;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileApiController {
    private final ProfileService profileService;
    private final S3Service s3Service;

    /**
     * 프로필 등록
     */
    @PostMapping("/profile/new") // @RequestBody ProfileInterestDto request
    public ResponseEntity<String> profileRegister(@RequestBody @Valid final RegisterRequest registerRequest, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        String loginUid = getSessionValue(session);

        String imgUrl = "https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png";
        String imgName = "default/default-profile.png";
        // 사진 등록
        if(!registerRequest.getFile().isEmpty()) {
            S3ResponseUploadEntity s3Entity = s3Service.upload(registerRequest.getFile(), "profile");
            imgUrl = s3Entity.getImageUrl();
            imgName = s3Entity.getImageName();
        }

        profileService.join(loginUid,
                new ProfileDto(registerRequest.getNickname(),imgUrl, imgName, registerRequest.getUsermessage(), registerRequest.getUinterest1(), registerRequest.getUinterest2(), registerRequest.getUinterest3(), registerRequest.getUinterest4(), registerRequest.getUinterest5()));

        removeSessionValue(session);

        return new ResponseEntity<>("Register login Success", HttpStatus.OK);

    }

    /**
     * 프로필 삭제
     */
    @PostMapping("/profile/delete")
    public ResponseEntity<String> profileDelete(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        String loginUid = getSessionValue(session);

        profileService.deleteOne(loginUid);

        removeSessionValue(session);

        return new ResponseEntity<>("Remove Success", HttpStatus.OK);
    }

    private String getSessionValue(HttpSession session){ //세션을 통해 loginUid값 찾기
        if (session == null) { //세션이 없으면 login 등록 페이지로 이동하게 null
            return null;
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) { //세션에 login 데이터가 없으면 login 등록 페이지로로 이동하게 null
            return null;
        }
        return loginResponse.getUid();
    }

    private void removeSessionValue(HttpSession session) { //세션 삭제
        session.removeAttribute(SessionConst.LOGIN_MEMBER); //회원가입 완료 후 세션 삭제
        session.invalidate(); //관련된 모든 session 속성 삭제
    }

    /**
     * 프로필 수정 폼
     */
    @GetMapping("/profile/{uid}/edit")
    public ProfileEditResponse profileEditForm(@PathVariable Long uid) {
        return profileService.getProfileEditForm(uid);
    }

    /**
     * 프로필 수정
     */
    @PutMapping("/profile/{uid}/edit")
    public ResponseEntity<String> profileEdit(@PathVariable Long uid,
                                              @RequestParam(required = false) MultipartFile file,
                                              @RequestParam(required = false) String usermessage,
                                              @RequestParam(required = false) List<String> interests) throws IOException {
        profileService.editProfile(uid, new ProfileEditRequest(file, usermessage, interests));
        return new ResponseEntity<>("Edit Profile Success", HttpStatus.OK);
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/profile/{uid}")
    public ProfileGetResponse getProfile(@PathVariable Long uid) {
        return profileService.getProfile(uid);
    }

    /**
     * 관심사가 동일한 프로필 목록 조회
     */
    @GetMapping("/profile/interests/{uid}")
    public SameAllInterestProfileResponse getSameInterestProfile(@PathVariable Long uid) {
        SameAllInterestProfileResponse profileSameInterests = profileService.getProfileSameInterests(uid);
        return profileSameInterests;
    }

    /**
     * 유저 검색에서 유저 닉네임을 통해 조회
     */
    @GetMapping("/profile/search/{nickname}")
    public List<SearchNicknameResultResponse> searchByNickname(@PathVariable String nickname) {
        return profileService.serachNickname(nickname);
    }
}
