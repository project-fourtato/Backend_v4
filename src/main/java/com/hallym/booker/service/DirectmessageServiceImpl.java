package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.exception.directmessage.NoSuchDirectmessageException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.DirectmessageRepository;
import com.hallym.booker.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DirectmessageServiceImpl implements DirectmessageService {

    @Autowired
    private DirectmessageRepository directmessageRepository;

    @Autowired
    private ProfileRepository profileRepository;

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
}
