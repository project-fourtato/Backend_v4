package com.hallym.booker.controller;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Follow.CountDto;
import com.hallym.booker.dto.Follow.ProfileDto;
import com.hallym.booker.dto.Result;
import com.hallym.booker.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;

    // 팔로잉 & 팔로워 추가
    @PostMapping("/follow/new")
    public ResponseEntity<String> addFollow(@RequestParam Long toUserId, @RequestParam Long fromUserId) {
        followService.newFollow(fromUserId,toUserId);
        return new ResponseEntity<>("Follow created successfully", HttpStatus.OK);
    }

    // 팔로잉 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/follow/followingsList/{fromUserId}")
    public Result followingsList(@PathVariable("fromUserId") Long fromUserId) {
        List<Profile> followings = followService.findAllToUserIdProfile(fromUserId);
        List<ProfileDto> followingsInfo = new ArrayList<>();
        for(Profile profile : followings){
            followingsInfo.add(new ProfileDto(profile.getProfileUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return new Result(followingsInfo);
    }

    // 팔로워 목록 조회 - 프로필(사진 이름, URL)&닉네임 (전체)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/follow/followersList/{toUserId}")
    public Result followersList(@PathVariable("toUserId") Long toUserId) {
        List<Profile> followers = followService.findAllFromUserIdProfile(toUserId);
        List<ProfileDto> followersInfo = new ArrayList<>();
        for(Profile profile : followers){
            followersInfo.add(new ProfileDto(profile.getProfileUid(),profile.getNickname(),
                    profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage()));
        }
        return new Result(followersInfo);
    }

    // 팔로잉들의 최신 독서록 목록 조회 - 프로필(사진 이름, URL)&닉네임
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/follow/followingsLatestJournals/{fromUserId}")
    public Result followingsLatestJournals(@PathVariable("fromUserId") Long fromUserId) {
        List<Journals> journalsList = followService.findFollowingsLatestJournals(fromUserId);
        // 최대 7개의 항목을 선택 (만약 리스트 크기가 5개보다 작으면 전체 리스트 반환)
        int numberOfJournalsToShow = Math.min(7, journalsList.size());
        return new Result(journalsList.subList(0, numberOfJournalsToShow));
    }

    // 팔로잉 삭제
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/follow/delete")
    public ResponseEntity<String> deleteFollow(@RequestParam Long toUserId, @RequestParam Long fromUserId) {
        followService.removeFollowing(fromUserId,toUserId);
        return new ResponseEntity<>("Follow deleted successfully",HttpStatus.OK);
    }

    //팔로잉 유무 확인
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/follow/followCheck/toUserId={toUserId}&fromUserId={fromUserId}")
    public Result followCheck(@PathVariable Long toUserId, @PathVariable Long fromUserId){
        return new Result(followService.checkFollowing(fromUserId,toUserId));
    }
}