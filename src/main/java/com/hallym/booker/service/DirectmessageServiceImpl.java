package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Directmessage.DirectmessageResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSenderRequest;
import com.hallym.booker.dto.Directmessage.GetDirectmessageResponse;
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
    private  final LoginRepository loginRepository;
    private final ProfileRepository profileRepository;
    private final DirectmessageRepository directmessageRepository;

    // 쪽지 목록 조회(프로필과 함께)
    @Override
    public List<DirectmessageResponse> getDirectmessageList(String loginUid, String userCheck) {
        Profile profile = profileRepository.findById(
                loginRepository.findById(loginUid).get().getProfile().getProfileUid()).orElseThrow(() -> new NoSuchProfileException());

        List<Directmessage> messageList;
        List<DirectmessageResponse> directmessageResponse;
        if(userCheck.equals("sender")) {
            messageList = directmessageRepository.findAllDirectmessagesBySender(
                    loginRepository.findById(loginUid).get().getProfile().getProfileUid());
            directmessageResponse = convertToDTO(messageList, false);
        } else {
            messageList = directmessageRepository.findAllDirectMessagesByRecipient(
                    loginRepository.findById(loginUid).get().getProfile().getProfileUid());
            directmessageResponse = convertToDTO(messageList, true);
        }

        Collections.sort(directmessageResponse, (msg1, msg2) -> msg2.getMdate().compareTo(msg1.getMdate()));

        return directmessageResponse;
    }

    private List<DirectmessageResponse> convertToDTO(List<Directmessage> messages, boolean isReceived) {
        List<DirectmessageResponse> dtos = new LinkedList<>();
        for (Directmessage message : messages) {
            Profile profile = profileRepository.findById(isReceived ? message.getSenderUid() : message.getRecipientUid())
                    .orElseThrow(() -> new NoSuchProfileException());

            DirectmessageResponse dto = DirectmessageResponse.builder()
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
    public void directmessageSend(DirectmessageSenderRequest directmessageSendRequest, String loginId) {
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);

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
    public GetDirectmessageResponse getDirectmessage(Long messageId, String loginId) {
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);
        Directmessage directmessage = directmessageRepository.findById(messageId).orElseThrow(NoSuchMessageException::new);

        if((directmessage.getRecipientUid() == login.getProfile().getProfileUid() && directmessage.isDeleteRecipientCheck()) ||
                directmessage.getSenderUid() == login.getProfile().getProfileUid() && directmessage.isDeleteSenderCheck()) {
            throw new NoSuchMessageException();
        }

        return new GetDirectmessageResponse(directmessage.getMessageId(), directmessage.getSenderUid(), directmessage.getRecipientUid(),
                directmessage.getMdate(), directmessage.getMcheck(), directmessage.getMtitle(), directmessage.getMcontents());
    }

    /**
     * 쪽지 삭제
     */
    @Transactional
    @Override
    public void directmessageDelete(Long messageId, String loginId) {
        Directmessage directmessage = directmessageRepository.findById(messageId).orElseThrow(NoSuchMessageException::new);
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);

        if(directmessage.getRecipientUid() == login.getProfile().getProfileUid()) {
            directmessage.changeDeleteRecipientCheck();
        } else {
            directmessage.changeDeleteSenderCheck();
        }

        if(directmessage.isDeleteSenderCheck() && directmessage.isDeleteRecipientCheck()) {
            directmessageRepository.deleteById(messageId);
        }
    }

    @Transactional
    @Override
    public void updateMcheck(Long messageid, int mcheck) {
        Directmessage directmessage = directmessageRepository.findById(messageid).orElseThrow(NoSuchMessageException::new);
        directmessage.changeMCheck(mcheck);
    }
}