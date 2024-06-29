package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Profile.RegisterRequest;
import com.hallym.booker.dto.Profile.S3Dto;
import com.hallym.booker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final InterestsRepository interestsRepository;
    private final FollowRepository followRepository;
    private final DirectmessageRepository directmessageRepository;

    /**
     *  프로필 등록
     */
    @Transactional
    public void join(String loginUid, RegisterRequest registerRequest, S3Dto s3Dto){
        Login l = loginRepository.findById(loginUid).get();

        Profile profile = Profile.create(l, registerRequest.getNickname(), s3Dto.getImageUrl(), s3Dto.getImageName(), registerRequest.getUsermessage());
        profile = profileRepository.save(profile);

        String[] interests_arr = new String[]{registerRequest.getUinterest1(),registerRequest.getUinterest2(),registerRequest.getUinterest3(),registerRequest.getUinterest4(),registerRequest.getUinterest5()};
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
    public void deleteOne(String loginUid){
        Login login = loginRepository.findById(loginUid).get();
        Profile profile = login.getProfile();
        List<Follow> followList = followRepository.findAllByToUserId(profile.getProfileUid());
        for (int i=0;i<followList.size();i++){
            followRepository.delete(followList.get(i));
        }
        List<Directmessage> directMessagesByRecipientList = directmessageRepository.findAllDirectMessagesByRecipient(profile.getProfileUid());
        for (int i=0;i<directMessagesByRecipientList.size();i++){
            Directmessage directmessage = directMessagesByRecipientList.get(i);
            directmessage.changeRecipientUid();
        }
        List<Directmessage> directMessagesBySenderList = directmessageRepository.findAllDirectmessagesBySender(profile.getProfileUid());
        for (int i=0;i<directMessagesBySenderList.size();i++){
            Directmessage directmessage = directMessagesBySenderList.get(i);
            directmessage.changeRecipientUid();
        }
        profileRepository.delete(profile);
    }

}
