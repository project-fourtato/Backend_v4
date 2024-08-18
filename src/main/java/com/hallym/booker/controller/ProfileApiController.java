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

@RestController
@RequiredArgsConstructor
public class ProfileApiController {
    private final ProfileService profileService;
    private final S3Service s3Service;

    /**
     * 프로필 등록
     */
    @PostMapping("/profile/new") // @RequestBody ProfileInterestDto request
    public ResponseEntity<String> profileRegister(@RequestParam(value = "file", required = false) MultipartFile file,
                                                    @RequestParam("nickname") String nickname,
                                                    @RequestParam("usermessage") String usermessage,
                                                    @RequestParam(name = "uinterest1", required = false) String uinterest1,
                                                  @RequestParam(name = "uinterest2", required = false) String uinterest2,
                                                  @RequestParam(name = "uinterest3", required = false) String uinterest3,
                                                  @RequestParam(name = "uinterest4", required = false) String uinterest4,
                                                  @RequestParam(name = "uinterest5", required = false) String uinterest5,
                                                  HttpServletRequest request) throws IOException {
        RegisterRequest registerRequest = new RegisterRequest(file, nickname,usermessage,uinterest1,uinterest2,uinterest3,uinterest4,uinterest5);
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        String loginUid = loginResponse.getUid();

        String imgUrl = "https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png";
        String imgName = "default/default-profile.png";
        // 사진 등록
        if(registerRequest.getFile() != null) {
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
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        profileService.deleteOne(loginResponse.getUid());

        removeSessionValue(session);

        return new ResponseEntity<>("Remove Success", HttpStatus.OK);
    }

    private void removeSessionValue(HttpSession session) { //세션 삭제
        session.removeAttribute(SessionConst.LOGIN_MEMBER); //회원가입 완료 후 세션 삭제
        session.invalidate(); //관련된 모든 session 속성 삭제
    }

    /**
     * 프로필 수정 폼
     */
    @GetMapping("/profile/edit")
    public ResponseEntity<ProfileEditResponse> profileEditForm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        return ResponseEntity.ok().body(profileService.getProfileEditForm(loginResponse.getUid()));
    }

    /**
     * 프로필 수정
     */
    @PutMapping("/profile/edit")
    public ResponseEntity<String> profileEdit(@RequestParam(required = false) MultipartFile file,
                                              @RequestParam(required = false) String usermessage,
                                              @RequestParam(required = false) List<String> interests,
                                              HttpServletRequest request) throws IOException {
        log.info(interests.get(0)+interests.get(1)+"여길까");
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        profileService.editProfile(loginResponse.getUid(), new ProfileEditRequest(file, usermessage, interests));
        return new ResponseEntity<>("Edit Profile Success", HttpStatus.OK);
    }

    /**
     * 프로필 조회
     */
    @GetMapping({"/profile", "/profile/{loginId}"})
    public ResponseEntity<ProfileGetResponse> getProfile(@PathVariable(required = false) String loginId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        String id = (loginId != null) ? loginId : loginResponse.getUid();
        return ResponseEntity.ok().body(profileService.getProfile(id));
    }

    /**
     * 관심사가 동일한 프로필 목록 조회
     */
    @GetMapping("/profile/interests")
    public ResponseEntity<SameAllInterestProfileResponse> getSameInterestProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse = (session == null) ? null : (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        return ResponseEntity.ok().body(profileService.getProfileSameInterests(loginResponse.getUid()));
    }

    /**
     * 유저 검색에서 유저 닉네임을 통해 조회
     */
    @GetMapping("/profile/search/{nickname}")
    public List<SearchNicknameResultResponse> searchByNickname(@PathVariable String nickname) {
        return profileService.serachNickname(nickname);
    }

    /**
     * 프로필 닉네임 중복검사
     */
    @GetMapping("/profile/checkNickname/{nickname}")
    public ResponseEntity<Void> duplicateNickname(@PathVariable String nickname) {
        profileService.duplicateNickname(nickname);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
