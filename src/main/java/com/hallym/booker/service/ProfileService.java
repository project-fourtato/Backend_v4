package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Profile.ProfileDto;
import com.hallym.booker.dto.Profile.ProfileEditRequest;
import com.hallym.booker.dto.Profile.ProfileEditResponse;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.exception.profile.TooManyInterestException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final InterestsRepository interestsRepository;
    private final FollowRepository followRepository;
    private final DirectmessageRepository directmessageRepository;
    private final S3Service s3Service;

    /**
     *  프로필 등록
     */
    @Transactional
    public void join(String loginUid, ProfileDto profileDto){
        Login l = loginRepository.findById(loginUid).get();

        Profile profile = Profile.create(l, profileDto.getNickname(), profileDto.getImageUrl(), profileDto.getImageName(), profileDto.getUsermessage());
        profile = profileRepository.save(profile);

        String[] interests_arr = new String[]{profileDto.getUinterest1(),profileDto.getUinterest2(),profileDto.getUinterest3(),profileDto.getUinterest4(),profileDto.getUinterest5()};
        for (int i =0;i<interests_arr.length;i++){
            if (interests_arr[i] != null){
                Interests interests = Interests.create(interests_arr[i],profile);
                interestsRepository.save(interests);
            }
        }
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteOne(String loginUid) throws IOException {
        Login login = loginRepository.findById(loginUid).get();
        Profile profile = login.getProfile();

        //사진 삭제
        if(!Objects.equals(profile.getUserimageName(), "/default/default-profile.png")) {
            s3Service.delete(profile.getUserimageName());
        }

        List<Follow> followList = followRepository.findAllByToUserId(profile.getProfileUid()); //딴 사람이 날 팔로우한 것을 취소해야 함
        for (Follow follow : followList){
            Profile following = profileRepository.findById(follow.getProfile().getProfileUid()).get();
            following.getFollow().remove(follow);
            followRepository.delete(follow);
        }
        List<Directmessage> directMessagesByRecipientList = directmessageRepository.findAllDirectMessagesByRecipient(profile.getProfileUid());
        for (int i=0;i<directMessagesByRecipientList.size();i++){
            Directmessage directmessage = directMessagesByRecipientList.get(i);
            directmessage.changeRecipientUid();
        }
        List<Directmessage> directMessagesBySenderList = directmessageRepository.findAllDirectmessagesBySender(profile.getProfileUid());
        for (int i=0;i<directMessagesBySenderList.size();i++){
            Directmessage directmessage = directMessagesBySenderList.get(i);
            directmessage.changeSenderUid();
        }

        profileRepository.deleteById(profile.getProfileUid());
    }

    /**
     * 프로필 수정 폼
     */
    @Transactional
    public ProfileEditResponse getProfileEditForm(Long uid) {
        Profile profile = profileRepository.findById(uid).orElseThrow(NoSuchProfileException::new);
        List<Interests> profileInterests = interestsRepository.findByProfile_ProfileUid(uid);

        return new ProfileEditResponse(profile.getNickname(), profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage(), profileInterests);
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public void editProfile(Long uid, ProfileEditRequest profileEdit) {
        Profile profile = profileRepository.findById(uid).orElseThrow(NoSuchProfileException::new);
        profile.change(profileEdit.getImageUrl(), profileEdit.getImageName(), profileEdit.getUsermessage());

        interestsRepository.deleteAll(profile.getInterests());
        interestsRepository.saveAll(profileEdit.getInterests());
    }
}
