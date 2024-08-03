package com.hallym.booker.service;

import com.hallym.booker.domain.Follow;
import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Follow.LatestJournalsResponse;
import com.hallym.booker.exception.follow.DuplicateFollowException;
import com.hallym.booker.repository.FollowRepository;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.LoginRepository;
import com.hallym.booker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final JournalsRepository journalsRepository;
    private final LoginRepository loginRepository;

    @Transactional
    @Override
    public void newFollow(Long fromUserId, Long toUserId){
        Boolean check = checkFollowing(fromUserId,toUserId);
        if (check == Boolean.TRUE){
            throw new DuplicateFollowException();
        }

        Profile from = profileRepository.findById(fromUserId).get();
        from.addFollowings();
        Profile to = profileRepository.findById(toUserId).get();
        to.addFollowers();

        Follow follow = Follow.create(from, toUserId);
        followRepository.save(follow);
    }

    @Override
    public List<Profile> findAllToUserIdProfile(String fromUserId){
        List<Follow> followList = followRepository.findAllByFromUserId(loginRepository.findById(fromUserId).get().getProfile().getProfileUid());
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(profileRepository.findById(follow.getToUserId()).get());
        }
        return profiles;
    }

    @Override
    public List<Profile> findAllFromUserIdProfile(String toUserId){
        List<Follow> followList = followRepository.findAllByToUserId(loginRepository.findById(toUserId).get().getProfile().getProfileUid());
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(follow.getProfile());
        }
        return profiles;
    }

    @Override
    public List<LatestJournalsResponse> findFollowingsLatestJournals(String fromUserId){
        List<Follow> followList = followRepository.findAllByFromUserId(loginRepository.findById(fromUserId).get().getProfile().getProfileUid());
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(profileRepository.findById(follow.getToUserId()).get());
        }
        List<LatestJournalsResponse> AllFollowingsjournals = new ArrayList<>();
        for (Profile profile: profiles){
            List<Journals> journals = journalsRepository.findByUserBooks_Profile_ProfileUidOrderByJdatetimeDesc(profile.getProfileUid());
            for(Journals journal : journals){
                LatestJournalsResponse l = LatestJournalsResponse.builder()
                        .toUserId(profile.getProfileUid())
                        .jid(journal.getJournalId())
                        .pdatetime(journal.getJdatetime())
                        .ptitle(journal.getJtitle())
                        .pcontents(journal.getJcontents())
                        .nickname(profile.getNickname())
                        .userimageUrl(profile.getUserimageUrl())
                        .userimageName(profile.getUserimageName())
                        .build();
                AllFollowingsjournals.add(l);
            }
        }
        int numberOfJournalsToShow = Math.min(7, AllFollowingsjournals.size()); //개수 제한
        return AllFollowingsjournals.subList(0,numberOfJournalsToShow);
    }

    @Transactional
    @Override
    public void removeFollowing(Long fromUserId,Long toUserId){
        profileRepository.findById(toUserId).get().removeFollowers();
        Profile from = profileRepository.findById(fromUserId).get();
        from.removeFollowings();
        from.removeFollow(followRepository.findByFromUserIdAndToUserId(fromUserId,toUserId).get());
    }

    @Override
    public Boolean checkFollowing(Long fromUserId,Long toUserId){
        Optional<Follow> follow= followRepository.findByFromUserIdAndToUserId(fromUserId,toUserId);
        if(follow.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}
