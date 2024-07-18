package com.hallym.booker.dto.directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DirectmessageGetResponse {
    Long messageId;
    Long senderUid;
    Long recipientUid;
    LocalDateTime mdate;
    Integer mcheck;
    String mtitle;
    String mcontents;
}
