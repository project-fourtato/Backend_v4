package com.hallym.booker.service;

import com.hallym.booker.domain.Follow;
import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Follow.LatestJournalsResponse;
import com.hallym.booker.exception.follow.DuplicateFollowException;
import com.hallym.booker.repository.FollowRepository;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface FollowService {

    public void newFollow(Long fromUserId, Long toUserId);
    public List<Profile> findAllToUserIdProfile(String fromUserId);
    public List<Profile> findAllFromUserIdProfile(String toUserId);
    public List<LatestJournalsResponse> findFollowingsLatestJournals(String fromUserId);
    public void removeFollowing(Long fromUserId,Long toUserId);
    public Boolean checkFollowing(Long fromUserId,Long toUserId);
    public Long findProfileUid(String LoginId);
}
