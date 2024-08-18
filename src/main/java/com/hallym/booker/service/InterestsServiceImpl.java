package com.hallym.booker.service;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.dto.Interests.InterestsResponseDTO;
import com.hallym.booker.repository.InterestsRepository;
import com.hallym.booker.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestsServiceImpl implements InterestsService {

    private final InterestsRepository interestsRepository;
    private final LoginRepository loginRepository;

    // 특정 프로필에 대한 모든 관심사 조회
    @Override
    @Transactional(readOnly = true)
    public List<String> getInterestsByProfile(String loginUid) {
        List<Interests> interests = interestsRepository.findByProfile_ProfileUid(
                loginRepository.findById(loginUid).get().getProfile().getProfileUid());
        if(interests.size()!= 5) {
            for (int i = interests.size(); i < 5; i++) {
                interests.add(null);
            }
        }
        return mapToDTOList(interests);
    }

    // DB에서 조회한 Interests 객체를 DTO 객체로 변환
    private List<String> mapToDTOList(List<Interests> interests) {
        List<String> interestsResponseDTOS = new ArrayList<>();
        for(int i=0;i<interests.size();i++){
            if(interests.get(i) == null){
                interestsResponseDTOS.add(null);
            }
            else{
                interestsResponseDTOS.add(interests.get(i).getInterestName());
            }
        }
        return interestsResponseDTOS;
    }
}