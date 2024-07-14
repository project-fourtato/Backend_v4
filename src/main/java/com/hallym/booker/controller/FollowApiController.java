package com.hallym.booker.controller;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Follow.FollowRequest;
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
    public ResponseEntity<String> addFollow(@RequestBody @Valid FollowRequest followRequest) {
        followService.newFollow(followRequest.getFromUserId(), followRequest.getToUserId());
        return new ResponseEntity<>("Follow created successfully", HttpStatus.OK);
    }

    // 팔로잉 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @PostMapping("/followingsList")
    public ResponseEntity<List<ProfileDto>> followingsList(@RequestBody Long fromUserId) {
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
    public ResponseEntity<List<ProfileDto>> followersList(@RequestBody Long toUserId) {
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
    public ResponseEntity<List<Journals>> followingsLatestJournals(@RequestBody Long fromUserId) {
        List<Journals> journalsList = followService.findFollowingsLatestJournals(fromUserId);
        // 최대 7개의 항목을 선택 (만약 리스트 크기가 5개보다 작으면 전체 리스트 반환)
        int numberOfJournalsToShow = Math.min(7, journalsList.size());
        return ResponseEntity.ok().body(journalsList.subList(0, numberOfJournalsToShow));
    }

    // 팔로잉 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteFollow(@RequestBody @Valid FollowRequest followRequest) {
        followService.removeFollowing(followRequest.getFromUserId(),followRequest.getToUserId());
        return new ResponseEntity<>("Follow deleted successfully",HttpStatus.OK);
    }

    //팔로잉 유무 확인
    @PostMapping("/followCheck")
    public ResponseEntity<Boolean> followCheck(@RequestBody @Valid FollowRequest followRequest){
        return ResponseEntity.ok().body(followService.checkFollowing(followRequest.getFromUserId(),followRequest.getToUserId()));
    }
}