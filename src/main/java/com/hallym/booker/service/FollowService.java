package com.hallym.booker.service;

import com.hallym.booker.domain.Follow;
import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.repository.FollowRepository;
import com.hallym.booker.repository.JournalsRepository;
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
public class FollowService {

    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final JournalsRepository journalsRepository;

    @Transactional
    public void newFollow(Long fromUserId, Long toUserId){
        Profile from = profileRepository.findById(fromUserId).get();
        from.addFollowings();
        Profile to = profileRepository.findById(toUserId).get();
        to.addFollowers();
        Follow follow = Follow.create(from, toUserId);
        followRepository.save(follow);
    }

    public List<Profile> findAllToUserIdProfile(Long fromUserId){
        List<Follow> followList = followRepository.findAllByFromUserId(fromUserId);
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(profileRepository.findById(follow.getToUserId()).get());
        }
        return profiles;
    }

    public List<Profile> findAllFromUserIdProfile(Long toUserId){
        List<Follow> followList = followRepository.findAllByToUserId(toUserId);
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(follow.getProfile());
        }
        return profiles;
    }

    public List<Journals> findFollowingsLatestJournals(Long fromUserId){
        List<Follow> followList = followRepository.findAllByFromUserId(fromUserId);
        List<Profile> profiles = new ArrayList<>();
        for (Follow follow : followList){
            profiles.add(profileRepository.findById(follow.getToUserId()).get());
        }
        List<Journals> AllFollowingsjournals = new ArrayList<>();
        for (Profile profile: profiles){
            List<Journals> journals = journalsRepository.findByUserBooks_Profile_ProfileUidOrderByJdatetimeDesc(profile.getProfileUid());
            for(Journals journal : journals){
                AllFollowingsjournals.add(journal);
            }
        }
        return AllFollowingsjournals;
    }

    @Transactional
    public void removeFollowing(Long fromUserId,Long toUserId){
        profileRepository.findById(toUserId).get().removeFollowers();
        Profile from = profileRepository.findById(fromUserId).get();
        from.removeFollowings();
        from.removeFollow(followRepository.findByFromUserIdAndToUserId(fromUserId,toUserId).get());
//        followRepository.deleteByToUserIdAndProfile_ProfileUid(fromUserId,toUserId);
    }

    public Boolean checkFollowing(Long fromUserId,Long toUserId){
        Optional<Follow> follow= followRepository.findByFromUserIdAndToUserId(fromUserId,toUserId);
        if(follow.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}
