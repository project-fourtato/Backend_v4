package com.hallym.booker.service;


import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.dto.Directmessage.DirectmessageGetResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSenderRequest;

import java.util.List;


public interface DirectmessageService {
    List<DirectmessageResponseDTO> getDirectmessageList(String loginUid, String userCheck);

    void directmessageSend(DirectmessageSenderRequest directmessageSendRequest, String loginId);

    DirectmessageGetResponse getDirectmessage(Long messageId);

    void directmessageDelete(Long messageId);

    void updateMcheck(Long messageid, int mcheck);

}