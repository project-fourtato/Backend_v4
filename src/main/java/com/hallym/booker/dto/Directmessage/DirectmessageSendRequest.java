package com.hallym.booker.dto.Directmessage;

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
