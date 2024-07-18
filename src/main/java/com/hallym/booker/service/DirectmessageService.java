package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.dto.directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.directmessage.DirectmessageSendRequest;
import com.hallym.booker.exception.directmessage.NoSuchMessageException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.DirectmessageRepository;
import com.hallym.booker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectmessageService {
    private final ProfileRepository profileRepository;
    private final DirectmessageRepository directmessageRepository;

    /**
     * 쪽지 등록
     */
    @Transactional
    public void directmessageSend(DirectmessageSendRequest directmessageSendRequest) {
        if(profileRepository.existsByProfileUid(directmessageSendRequest.getRecipientUid())) {
            Directmessage directmessage = Directmessage.create(0, directmessageSendRequest.getMtitle(),
                    directmessageSendRequest.getMcontents(), LocalDateTime.now(),
                    directmessageSendRequest.getSenderUid(), directmessageSendRequest.getRecipientUid());

            directmessageRepository.save(directmessage);
        } else {
            throw new NoSuchProfileException();
        }
    }

    /**
     * 쪽지 조회
     */
    public DirectmessageGetResponse getDirectmessage(Long messageId) {
        Directmessage directmessage = directmessageRepository.findById(messageId).orElseThrow(NoSuchMessageException::new);

        return new DirectmessageGetResponse(directmessage.getMessageId(), directmessage.getSenderUid(), directmessage.getRecipientUid(),
                directmessage.getMdate(), directmessage.getMcheck(), directmessage.getMtitle(), directmessage.getMcontents());
    }
}
