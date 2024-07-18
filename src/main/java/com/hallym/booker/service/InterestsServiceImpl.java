package com.hallym.booker.service;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.dto.Interests.InterestsResponseDTO;
import com.hallym.booker.repository.InterestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestsServiceImpl implements InterestsService {

    private final InterestsRepository interestsRepository;

    // 특정 프로필에 대한 모든 관심사 조회
    @Override
    @Transactional(readOnly = true)
    public List<InterestsResponseDTO> getInterestsByProfile(Long profileUid) {
        List<Interests> interests = interestsRepository.findByProfile_ProfileUid(profileUid);
        return mapToDTOList(interests);
    }

    // 모든 관심사들 조회(본인 제외)
    @Override
    @Transactional(readOnly = true)
    public List<InterestsResponseDTO> getAllInterestsExceptProfile(Long profileUid) {
        List<Interests> interests = interestsRepository.findByProfile_ProfileUidNotIn(profileUid);
        return mapToDTOList(interests);
    }

    // DB에서 조회한 Interests 객체를 DTO 객체로 변환
    private List<InterestsResponseDTO> mapToDTOList(List<Interests> interests) {
        return interests.stream()
                .map(interest -> InterestsResponseDTO.builder()
                        .interestUid(interest.getInterestUid())
                        .interestName(interest.getInterestName())
                        .build())
                .collect(Collectors.toList());
    }
}
