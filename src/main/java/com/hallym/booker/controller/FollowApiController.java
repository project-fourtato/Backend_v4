package com.hallym.booker.controller;

import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Follow.FollowRequest;
import com.hallym.booker.dto.Follow.LatestJournalsResponse;
import com.hallym.booker.dto.Follow.ProfileDto;
import com.hallym.booker.service.FollowService;
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
    public ResponseEntity<String> addFollow(@RequestBody @Valid final FollowRequest followRequest) {
        followService.newFollow(followRequest.getFromUserId(), followRequest.getToUserId());
        return new ResponseEntity<>("Follow created successfully", HttpStatus.OK);
    }

    // 팔로잉 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @PostMapping("/followingsList")
    public ResponseEntity<List<ProfileDto>> followingsList(@RequestBody final Long fromUserId) {
        List<Profile> followings = followService.findAllToUserIdProfile(fromUserId);
        List<ProfileDto> followingsInfo = new ArrayList<>();
        for(Profile profile : followings){
            followingsInfo.add(new ProfileDto(profile.getProfileUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return ResponseEntity.ok().body(followingsInfo);
    }

    // 팔로워 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @PostMapping("/followersList")
    public ResponseEntity<List<ProfileDto>> followersList(@RequestBody final Long toUserId) {
        List<Profile> followers = followService.findAllFromUserIdProfile(toUserId);
        List<ProfileDto> followersInfo = new ArrayList<>();
        for(Profile profile : followers){
            followersInfo.add(new ProfileDto(profile.getProfileUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return ResponseEntity.ok().body(followersInfo);
    }

    // 팔로잉들의 최신 독서록 목록 조회 - 프로필(사진 이름, URL)&닉네임
    @PostMapping("/followingsLatestJournals")
    public ResponseEntity<List<LatestJournalsResponse>> followingsLatestJournals(@RequestBody final Long fromUserId) {
        return ResponseEntity.ok().body(followService.findFollowingsLatestJournals(fromUserId));
    }

    // 팔로잉 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteFollow(@RequestBody @Valid final FollowRequest followRequest) {
        followService.removeFollowing(followRequest.getFromUserId(),followRequest.getToUserId());
        return new ResponseEntity<>("Follow deleted successfully",HttpStatus.OK);
    }

    //팔로잉 유무 확인
    @PostMapping("/followCheck")
    public ResponseEntity<Boolean> followCheck(@RequestBody @Valid final FollowRequest followRequest){
        return ResponseEntity.ok().body(followService.checkFollowing(followRequest.getFromUserId(),followRequest.getToUserId()));
    }
}