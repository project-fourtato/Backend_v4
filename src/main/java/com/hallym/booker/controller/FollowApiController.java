package com.hallym.booker.controller;

import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.SessionConst;
import com.hallym.booker.dto.Follow.FollowRequest;
import com.hallym.booker.dto.Follow.LatestJournalsResponse;
import com.hallym.booker.dto.Follow.ProfileDto;
import com.hallym.booker.dto.Login.LoginResponse;
import com.hallym.booker.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;


    // 팔로잉 & 팔로워 추가
    @PostMapping("/new")
    public ResponseEntity<String> addFollow(@RequestBody @Valid final FollowRequest followRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        followService.newFollow(followRequest.getToUserId(), followService.findProfileUid(loginResponse.getUid()));
        return new ResponseEntity<>("Follow created successfully", HttpStatus.OK);
    }


    // 팔로잉 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @PostMapping("/followingsList")
    public ResponseEntity<List<ProfileDto>> followingsList(@RequestBody @Valid final FollowRequest followRequest,HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        List<Profile> followings = followService.findAllToUserIdProfile(followRequest.getToUserId());
        List<ProfileDto> followingsInfo = new ArrayList<>();
        for(Profile profile : followings){
            followingsInfo.add(new ProfileDto(profile.getLogin().getLoginUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return ResponseEntity.ok().body(followingsInfo);
    }

    // 팔로워 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @PostMapping("/followersList")
    public ResponseEntity<List<ProfileDto>> followersList(@RequestBody @Valid final FollowRequest followRequest,HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }

        List<Profile> followers = followService.findAllFromUserIdProfile(followRequest.getFromUserId());
        List<ProfileDto> followersInfo = new ArrayList<>();
        for(Profile profile : followers){
            followersInfo.add(new ProfileDto(profile.getLogin().getLoginUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return ResponseEntity.ok().body(followersInfo);
    }

    // 팔로잉들의 최신 독서록 목록 조회 - 프로필(사진 이름, URL)&닉네임
    @GetMapping("/followingsLatestJournals")
    public ResponseEntity<List<LatestJournalsResponse>> followingsLatestJournals(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        return ResponseEntity.ok().body(followService.findFollowingsLatestJournals(loginResponse.getUid()));
    }

    // 팔로잉 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteFollow(@RequestBody @Valid final FollowRequest followRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        followService.removeFollowing(followRequest.getToUserId(), followService.findProfileUid(loginResponse.getUid()));
        return new ResponseEntity<>("Follow deleted successfully",HttpStatus.OK);
    }

    //팔로잉 유무 확인
    @PostMapping("/followCheck")
    public ResponseEntity<Boolean> followCheck(@RequestBody @Valid final FollowRequest followRequest, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){ //세션이 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        LoginResponse loginResponse = (LoginResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginResponse == null){ //세션에 회원 데이터가 없으면 홈으로 이동하게 null
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
        return ResponseEntity.ok().body(followService.checkFollowing(followRequest.getToUserId(),followService.findProfileUid(loginResponse.getUid())));
    }
}