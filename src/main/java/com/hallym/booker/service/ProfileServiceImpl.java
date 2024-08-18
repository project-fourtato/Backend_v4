package com.hallym.booker.service;

import com.hallym.booker.domain.*;
import com.hallym.booker.dto.Profile.*;
import com.hallym.booker.exception.profile.DuplicateNicknameException;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
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
        Login l ;
        try {
            l = loginRepository.findById(loginUid).get();
        } catch (NoSuchElementException e){
            throw new NoSuchLoginException();
        }

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
        Login login ;
        try {
            login = loginRepository.findById(loginUid).get();
        } catch (NoSuchElementException e){
            throw new NoSuchLoginException();
        }
        Profile profile = login.getProfile();

        //사진 삭제
        if(!Objects.equals(profile.getUserimageName(), "/default/default-profile.png")) {
            s3Service.delete(profile.getUserimageName());
        }

        List<Follow> followList = followRepository.findAllByToUserId(profile.getProfileUid()); //딴 사람이 날 팔로우한 것을 취소해야 함
        for (Follow follow : followList){
            Profile following = profileRepository.findById(follow.getProfile().getProfileUid()).get();
            following.getFollow().remove(follow);
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
    public ProfileEditResponse getProfileEditForm(String loginId) {
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);
        Long uid = login.getProfile().getProfileUid();

        Profile profile = profileRepository.findById(uid).orElseThrow(NoSuchProfileException::new);
        List<Interests> profileInterests = interestsRepository.findByProfile_ProfileUid(uid);

        List<String> interestsList = new ArrayList<>();
        for (Interests profileInterest : profileInterests) {
            interestsList.add(profileInterest.getInterestName());
        }
        return new ProfileEditResponse(profile.getNickname(), profile.getUserimageUrl(), profile.getUserimageName(), profile.getUsermessage(), interestsList);
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public void editProfile(String loginId, ProfileEditRequest profileEdit) throws IOException {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();

        S3ResponseUploadEntity uploadEntity = new S3ResponseUploadEntity(profile.getUserimageName(), profile.getUserimageUrl());
        if(profileEdit.getFile() != null) {
            if(!profile.getUserimageName().equals("/default/default-profile.png")) {
                s3Service.delete(profile.getUserimageName());
            }
            uploadEntity = s3Service.upload(profileEdit.getFile(), "profile");
        }

        //profile 갱신
        profile.change(uploadEntity.getImageUrl(), uploadEntity.getImageName(), profileEdit.getUsermessage());

        //interests 갱신
        if(profileEdit.getInterests() != null) {
            List<Interests> interestsList = new ArrayList<>(profile.getInterests());

            for (Interests interests : interestsList) {
                interests.removeProfile(profile);
            }
            interestsRepository.deleteAllByProfile_ProfileUid(profile.getProfileUid());

            for (String interest : profileEdit.getInterests()) {
                Interests interests = Interests.create(interest, profile);
                interestsRepository.save(interests);
            }
        }
    }

    /**
     * 프로필 조회
     */
    public ProfileGetResponse getProfile(String loginId) {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();

        List<String> interestsList = new ArrayList<>();
        for (Interests interest : profile.getInterests()) {
            interestsList.add(interest.getInterestName());
        }

        return new ProfileGetResponse(profile.getProfileUid(), profile.getNickname(), profile.getUserimageUrl(), profile.getUserimageName()
        ,profile.getUsermessage(), profile.getCountFollowers(), profile.getCountFollowings(), interestsList);
    }

    /**
     * 관심사가 동일한 프로필 목록 조회
     */
    public SameAllInterestProfileResponse getProfileSameInterests(String loginId) {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();

        List<Profile> sameInterestProfile = interestsRepository.findSameInterestProfile(profile.getProfileUid());
        sameInterestProfile.remove(profile);

        return SameAllInterestProfileResponse.from(sameInterestProfile);
    }

    /**
     * 유저 검색에서 유저 닉네임을 통해 조회
     */
    public List<SearchNicknameResultResponse> serachNickname(String nickname) {
        List<Profile> allByNickname = profileRepository.findAllByNickname(nickname);

        List<SearchNicknameResultResponse> allSearchByNickname = new ArrayList<>();
        for (Profile profile : allByNickname) {
            allSearchByNickname.add(new SearchNicknameResultResponse(profile.getProfileUid(), profile.getLogin().getLoginUid(),
                    profile.getNickname(), profile.getUserimageUrl(), profile.getUsermessage()));
        }

        return allSearchByNickname;
    }

    /**
     * 닉네임 중복 확인
     */
    public void duplicateNickname(String nickname) {
        if(profileRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }
}
