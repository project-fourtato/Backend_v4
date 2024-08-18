package com.hallym.booker.service;


import com.hallym.booker.dto.Directmessage.DirectmessageResponse;
import com.hallym.booker.dto.Directmessage.DirectmessageSenderRequest;
import com.hallym.booker.dto.Directmessage.GetDirectmessageResponse;

import java.util.List;


public interface DirectmessageService {
    List<DirectmessageResponse> getDirectmessageList(String loginUid, String userCheck);

    void directmessageSend(DirectmessageSenderRequest directmessageSendRequest, String loginId);

    GetDirectmessageResponse getDirectmessage(Long messageId);

    void directmessageDelete(Long messageId);

    void updateMcheck(Long messageid, int mcheck);

}