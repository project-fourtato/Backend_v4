package com.hallym.booker.service;


import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.dto.Directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSendRequest;

import java.util.List;


public interface DirectmessageService {
    List<DirectmessageResponseDTO> getDirectmessageList(String loginUid);

    void directmessageSend(DirectmessageSendRequest directmessageSendRequest);

    DirectmessageGetResponse getDirectmessage(Long messageId);

    void directmessageDelete(Long messageId);
}