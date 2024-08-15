package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.dto.Directmessage.DirectmessageSendRequest;
import com.hallym.booker.exception.directmessage.NoSuchDirectmessageException;
import com.hallym.booker.exception.directmessage.NoSuchMessageException;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.DirectmessageRepository;
import com.hallym.booker.repository.LoginRepository;
import com.hallym.booker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DirectmessageServiceImpl implements DirectmessageService{
    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final DirectmessageRepository directmessageRepository;

    // 쪽지 목록 조회(프로필과 함께)
    @Override
    public List<DirectmessageResponseDTO> getDirectmessageList(Long profileUid) {
        Profile profile = profileRepository.findById(profileUid)
                .orElseThrow(() -> new NoSuchProfileException());

        List<Directmessage> receivedMessages = directmessageRepository.findAllDirectMessagesByRecipient(profileUid);
        List<Directmessage> sentMessages = directmessageRepository.findAllDirectmessagesBySender(profileUid);

        if (receivedMessages.isEmpty() && sentMessages.isEmpty()) {
            throw new NoSuchDirectmessageException();
        }

        List<DirectmessageResponseDTO> allMessages = new LinkedList<>();
        allMessages.addAll(convertToDTO(receivedMessages, true));
        allMessages.addAll(convertToDTO(sentMessages, false));

        Collections.sort(allMessages, (msg1, msg2) -> msg2.getMdate().compareTo(msg1.getMdate()));

        return allMessages;
    }

    private List<DirectmessageResponseDTO> convertToDTO(List<Directmessage> messages, boolean isReceived) {
        List<DirectmessageResponseDTO> dtos = new LinkedList<>();
        for (Directmessage message : messages) {
            Profile profile = profileRepository.findById(isReceived ? message.getSenderUid() : message.getRecipientUid())
                    .orElseThrow(() -> new NoSuchProfileException());

            DirectmessageResponseDTO dto = DirectmessageResponseDTO.builder()
                    .messageId(message.getMessageId())
                    .senderUid(message.getSenderUid())
                    .recipientUid(message.getRecipientUid())
                    .mdate(message.getMdate())
                    .mcheck(message.getMcheck())
                    .mtitle(message.getMtitle())
                    .mcontents(message.getMcontents())
                    .nickname(profile.getNickname())
                    .userimageUrl(profile.getUserimageUrl())
                    .userimageName(profile.getUserimageName())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 쪽지 등록
     */
    @Transactional
    @Override
    public void directmessageSend(DirectmessageSendRequest directmessageSendRequest, String loginId) {
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);
        log.info("여기!! {}", directmessageSendRequest.getRecipientUid());
        if(profileRepository.existsByProfileUid(directmessageSendRequest.getRecipientUid())) {
            Directmessage directmessage = Directmessage.create(0, directmessageSendRequest.getMtitle(),
                    directmessageSendRequest.getMcontents(), LocalDateTime.now(),
                    login.getProfile().getProfileUid(), directmessageSendRequest.getRecipientUid());

            directmessageRepository.save(directmessage);
        } else {
            throw new NoSuchProfileException();
        }
    }

    /**
     * 쪽지 조회
     */
    @Override
    public DirectmessageGetResponse getDirectmessage(Long messageId) {
        Directmessage directmessage = directmessageRepository.findById(messageId).orElseThrow(NoSuchMessageException::new);

        return new DirectmessageGetResponse(directmessage.getMessageId(), directmessage.getSenderUid(), directmessage.getRecipientUid(),
                directmessage.getMdate(), directmessage.getMcheck(), directmessage.getMtitle(), directmessage.getMcontents());
    }

    /**
     * 쪽지 삭제
     */
    @Transactional
    @Override
    public void directmessageDelete(Long messageId) {
        if(directmessageRepository.existsByMessageId(messageId)) {
            directmessageRepository.deleteById(messageId);
        } else {
            throw new NoSuchMessageException();
        }
    }
}
