package com.hallym.booker.dto.directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectmessageSendRequest {
    Long senderUid;
    Long recipientUid;
    String mtitle;
    String mcontents;
}
